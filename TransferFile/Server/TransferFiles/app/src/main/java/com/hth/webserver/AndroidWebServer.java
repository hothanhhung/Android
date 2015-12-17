package com.hth.webserver;

import android.app.Activity;

import com.google.gson.Gson;
import com.hth.data.Authentication;
import com.hth.data.FolderInfo;
import com.hth.filestransfer.R;
import com.hth.utils.DataSevices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

/**
 * Created by Lenovo on 12/4/2015.
 */
public class AndroidWebServer extends  NanoHTTPD {

    private Activity activity;
    public AndroidWebServer()
    {
        super(0);
    }

    public void startAndTryPort(int port, Activity activity) throws IOException
    {
        this.activity = activity;
        if(isLocalPortInUse(port)) {
            setMyPort(0);
            start();
        }else{
            setMyPort(port);
            start();
        }
    }

    private boolean isLocalPortInUse(int port) {
        try {
            // ServerSocket try to open a LOCAL port
            new ServerSocket(port).close();
            // local port can be opened, it's available
            return false;
        } catch(IOException e) {
            // local port cannot be opened, it's in use
            return true;
        }
    }
/*
    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        Map<String, String> parms = session.getParms();
        if (parms.get("username") == null) {
            msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
        } else {
            msg += "<p>Hello, " + parms.get("username") + "!</p>";
        }
        return newFixedLengthResponse( msg + "</body></html>\n" );

    }
*/
    @Override
    public Response serve(String uri, Method method,
                          Map<String, String> header,
                          Map<String, String> parameters,
                          Map<String, String> files) {

        if(parameters.size() > 1 && uri != null && uri.equalsIgnoreCase("/"))
            return responseOfAPI(uri, method, header, parameters, files);

        if(uri == null || uri.isEmpty() || uri.equalsIgnoreCase("/")) uri = "/index.html";
        try {
            Enumeration<URL> resources = NanoHTTPD.class.getClassLoader().getResources("Website" + uri);

            if(resources.hasMoreElements()) {
                URL url = (URL) resources.nextElement();
                InputStream inputStream = null;
                try {
                    inputStream = url.openStream();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return newChunkedResponse(Response.Status.OK, getMimeTypeForFile(uri),inputStream);
            }else{
                return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Not Found");
            }

        } catch(Exception e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, e.getMessage());
        }


    }

    private Response responseOfAPI(String uri, Method method, Map<String, String> header, Map<String, String> parameters, Map<String, String> files)
    {
        String apiName = parameters.get("api");
        switch (apiName.toLowerCase())
        {
            case "auth":
                return getAuthenticationResponse();
            case "browser":
                return getDirInfo(uri, parameters);
            case "upload":
                return uploadFiles(uri, method, header, parameters, files);
            case "rename":
                return rename(uri, method, header, parameters);
            case "delete":
                return delete(uri, method, header, parameters);
            case "get":
                return sendFile(uri, method, header, parameters);

        }
        return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "API Not found");
    }

    private Response sendFile(String uri, Method method, Map<String, String> header, Map<String, String> parameters)
    {
        try {
            if(!DataSevices.hasAllowWithKey(activity, R.string.pref_sync_allow_download)) {
                return newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED, NanoHTTPD.MIME_PLAINTEXT, "Not Allowed");
            }

            String fullPath = parameters.get("path");
            File file = new File(fullPath);

            if(file.exists()) {
                InputStream inputStream = new FileInputStream(file);
                NanoHTTPD.Response res = newChunkedResponse(Response.Status.OK, "application/octet-stream",inputStream);
                res.addHeader("Content-Disposition", "attachment; filename="+ file.getName());
                return res;
            }else{
                return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Not Found");
            }

        } catch(Exception e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, e.getMessage());
        }
    }

    private Response delete(String uri, Method method, Map<String, String> header, Map<String, String> parameters)
    {
        try {

            if(!DataSevices.hasAllowWithKey(activity, R.string.pref_sync_allow_delete)) {
                return newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED, NanoHTTPD.MIME_PLAINTEXT, "Unauthorized");
            }

            String fullPath = parameters.get("path");
            File file = new File(fullPath);
            if(file.exists()) {
                if (file.delete()) {
                    return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "Delete successfully");
                } else {
                    return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "Delete unsuccessfully");
                }
            }else {
                return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "File not found");
            }
        } catch(Exception e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, e.getMessage());
        }
    }


    private Response rename(String uri, Method method, Map<String, String> header, Map<String, String> parameters)
    {
        try {
            if(!DataSevices.hasAllowWithKey(activity, R.string.pref_sync_allow_rename)) {
                return newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED, NanoHTTPD.MIME_PLAINTEXT, "Not Allowed");
            }
            String fullPath = parameters.get("path");
            String newName = parameters.get("newName");
            if(fullPath == null || fullPath.isEmpty() || newName==null || newName.isEmpty())
            {
                return newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT,"missing parameters");
            }
            File file = new File(fullPath);
            if(file.exists()) {
                File newfile = new File(file.getParent() + "/" + newName);
                file.renameTo(newfile);
                return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT,"Rename successfully");
            }else{
                return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Not Found");
            }
        } catch(Exception e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, e.getMessage());
        }
    }

    private Response uploadFiles(String uri, Method method, Map<String, String> header, Map<String, String> parameters,Map<String, String> files)
    {
        try {
            if(!DataSevices.hasAllowWithKey(activity, R.string.pref_sync_allow_upload)) {
                return newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED, NanoHTTPD.MIME_PLAINTEXT, "Not Allowed");
            }

            String fullPath = parameters.get("path");
            Set<String> keys = files.keySet();
            for(String key: keys){
                String name = parameters.get("file");
                String loaction = files.get(key);
                File tempfile = new File(loaction);
                File toFile = new File(fullPath+"/"+name);
                copy(tempfile, toFile);
                tempfile.deleteOnExit();
              //  tempfile.renameTo(new File(fullPath+"/"+name));
            }
            return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "Ok");

        } catch (Exception e) {
            System.out.println("i am error file upload post ");
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, e.getMessage());
        }
    }

    private Response getDirInfo(String uri, Map<String, String> parameters)
    {
        String fullPath = parameters.get("path");
        if(fullPath!=null && DataSevices.isExist(fullPath)) {
            ArrayList<FolderInfo> obj = DataSevices.getDirInfo(fullPath);
            Gson gson = new Gson();
            String json = gson.toJson(obj);
            return newFixedLengthResponse(Response.Status.OK, "application/json", json);
        }else{
            return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Not found");
        }
    }

    private Response getAuthenticationResponse(){
        Authentication obj = getAuthentication();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        return newFixedLengthResponse(Response.Status.OK, "application/json", json);
    }

    private Authentication getAuthentication() {
        Authentication obj = new Authentication();
        obj.AllowUpload = DataSevices.hasAllowWithKey(activity, R.string.pref_sync_allow_upload);
        obj.AllowDownload = DataSevices.hasAllowWithKey(activity, R.string.pref_sync_allow_download);
        obj.AllowRename = DataSevices.hasAllowWithKey(activity, R.string.pref_sync_allow_rename);
        obj.AllowDelete = DataSevices.hasAllowWithKey(activity, R.string.pref_sync_allow_delete);
        obj.EnableKey = DataSevices.hasAllowWithKey(activity, R.string.pref_sync_enable_key);
        return  obj;
    }
    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
/*
    private Response defaultRespond(Map<String, String> headers, IHTTPSession session, String uri) {
        // Remove URL arguments
        uri = uri.trim().replace(File.separatorChar, '/');
        if (uri.indexOf('?') >= 0) {
            uri = uri.substring(0, uri.indexOf('?'));
        }

        // Prohibit getting out of current directory
        if (uri.contains("../")) {
            return getForbiddenResponse("Won't serve ../ for security reasons.");
        }

        boolean canServeUri = false;
        File homeDir = null;
        for (int i = 0; !canServeUri && i < this.rootDirs.size(); i++) {
            homeDir = this.rootDirs.get(i);
            canServeUri = canServeUri(uri, homeDir);
        }
        if (!canServeUri) {
            return getNotFoundResponse();
        }

        // Browsers get confused without '/' after the directory, send a
        // redirect.
        File f = new File(homeDir, uri);
        if (f.isDirectory() && !uri.endsWith("/")) {
            uri += "/";
            Response res =
                    newFixedLengthResponse(Response.Status.REDIRECT, NanoHTTPD.MIME_HTML, "<html><body>Redirected: <a href=\"" + uri + "\">" + uri + "</a></body></html>");
            res.addHeader("Location", uri);
            return res;
        }

        if (f.isDirectory()) {
            // First look for index files (index.html, index.htm, etc) and if
            // none found, list the directory if readable.
            String indexFile = findIndexFileInDirectory(f);
            if (indexFile == null) {
                if (f.canRead()) {
                    // No index file, list the directory if it is readable
                    return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, listDirectory(uri, f));
                } else {
                    return getForbiddenResponse("No directory listing.");
                }
            } else {
                return respond(headers, session, uri + indexFile);
            }
        }
        String mimeTypeForFile = getMimeTypeForFile(uri);
        WebServerPlugin plugin = SimpleWebServer.mimeTypeHandlers.get(mimeTypeForFile);
        Response response = null;
        if (plugin != null && plugin.canServeUri(uri, homeDir)) {
            response = plugin.serveFile(uri, headers, session, f, mimeTypeForFile);
            if (response != null && response instanceof InternalRewrite) {
                InternalRewrite rewrite = (InternalRewrite) response;
                return respond(rewrite.getHeaders(), session, rewrite.getUri());
            }
        } else {
            response = serveFile(uri, headers, f, mimeTypeForFile);
        }
        return response != null ? response : getNotFoundResponse();
    }

    protected Response getForbiddenResponse(String s) {
        return newFixedLengthResponse(Response.Status.FORBIDDEN, NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: " + s);
    }

    protected Response getInternalErrorResponse(String s) {
        return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "INTERNAL ERROR: " + s);
    }

    protected Response getNotFoundResponse() {
        return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Error 404, file not found.");
    }
/*
    private String loadWebsite()
    {
        try {
            Enumeration<URL> resources = AndroidWebServer.class.getClassLoader().getResources("Website/index.html");
            while (resources.hasMoreElements()) {
                URL url = (URL) resources.nextElement();
                Properties properties = new Properties();
                InputStream stream = null;
                try {
                    stream = url.openStream();
                    properties.load(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    safeClose(stream);
                }
                result.putAll((Map) properties);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}

