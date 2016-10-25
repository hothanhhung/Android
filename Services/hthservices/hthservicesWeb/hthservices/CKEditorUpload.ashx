<%@ WebHandler Language="C#" Class="CKEditorUpload" %>
using System;
using System.Web;

public class CKEditorUpload : IHttpHandler{    
    public void ProcessRequest (HttpContext context) {
       HttpPostedFile uploads = context.Request.Files["upload"];
       string CKEditorFuncNum = context.Request["CKEditorFuncNum"];
       string file = System.IO.Path.GetFileName(uploads.FileName);
       uploads.SaveAs(context.Server.MapPath(".") + "\\ImagesUpload\\"+ file);
       //provide direct URL here
       string url = "http://localhost/CKeditorDemo/Images/"+ file;  
        
       context.Response.Write("<script>window.parent.CKEDITOR.tools.callFunction("+ CKEditorFuncNum +       ", \"" + url + "\");</script>");
       context.Response.End();             
    }

    public bool IsReusable {
        get { return false; }
    }
}