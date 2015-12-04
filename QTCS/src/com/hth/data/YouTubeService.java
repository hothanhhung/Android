package com.hth.data;

import java.io.ByteArrayOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.StrictMode;

import com.google.gson.Gson;

public class YouTubeService {
	
	public static YouTubeParserJSObject getRecentPublishVideo()
	{
		return searchPublishedVideos("", "", "", "date", "", "", true);
	}
	
	public static YouTubeParserJSObject getOlderPublishYouTubeVideo(String publishedBefore)
	{
		return searchPublishedVideos("", "", publishedBefore, "date", "", "7", false);
	}
	
	public static YouTubeParserJSObject searchPublishedVideos(String query, String publishedAfter, String publishedBefore, String order, String pageToken)
	{
		return searchPublishedVideos(query, publishedAfter, publishedBefore, order, pageToken, "20", true);
	}
	
	public static YouTubeParserJSObject searchPublishedVideos(String query, String publishedAfter, String publishedBefore, String order, String pageToken, String maxResults, boolean needCorrectDate)
	{
		YouTubeParserJSObject results = null;
		try
		{
			//String url_select = "https://www.googleapis.com/youtube/v3/search?channelId=UCn-WnIG_oFOd11uKHESF_9w&part=snippet,id&q=&order=date&maxResults=20&key=AIzaSyBMhBxnjBMMFOeqO_nqw7TyFf0jN07CHKw";
			String url_select = buildUrlRequest(query,publishedAfter, publishedBefore, order, pageToken, maxResults, needCorrectDate);
			
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			
			HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(url_select));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        String responseString = out.toString();
		        Gson gson = new Gson();
		        results = gson.fromJson(responseString, YouTubeParserJSObject.class); 
		        //..more logic
		    } else{
		        //Closes the connection.
		        response.getEntity().getContent().close();
		        //throw new IOException(statusLine.getReasonPhrase());
		    }
            
		}catch (Exception ex) {ex.printStackTrace();}
		return results;
	}
	
	public static YouTubeVideoDetail getDetailsMovie(String ID)
	{
		YouTubeVideoDetail youTubeVideo = null;
		try
		{
			String url_select = "https://www.googleapis.com/youtube/v3/videos?part=statistics,snippet&id="+ID+"&key="+DeveloperConst.DEVELOPER_KEY;
			
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			
			HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(url_select));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        String responseString = out.toString();
		        Gson gson = new Gson();
		        YouTubeDetailParserJSObject results = gson.fromJson(responseString, YouTubeDetailParserJSObject.class); 
		        if(results!=null && results.items!=null && results.items.length > 0) youTubeVideo = results.items[0];
		        //..more logic
		    } else{
		        //Closes the connection.
		        response.getEntity().getContent().close();
		        //throw new IOException(statusLine.getReasonPhrase());
		    }
            
		}catch (Exception ex) {ex.printStackTrace();}
		return youTubeVideo;
	}
	//UCn-WnIG_oFOd11uKHESF_9w
	public static String buildUrlRequest(String query, String publishedAfter, String publishedBefore, String order, String pageToken, String maxResults, boolean needCorrectDate)
	{
		String url_select = "https://www.googleapis.com/youtube/v3/search?channelId=UCFWy6mvqbTFxu45PEndiVeA&part=snippet,id";//&maxResults=20&q=&order=date&maxResults=20&key=AIzaSyBMhBxnjBMMFOeqO_nqw7TyFf0jN07CHKw";
		
		url_select += "&key=" + DeveloperConst.DEVELOPER_KEY;
		
		if(query!="") url_select += "&q=" + query;

		if(needCorrectDate) publishedAfter = getCorrectDateTimeForRequest(publishedAfter, false);
		if(publishedAfter!="") url_select += "&publishedAfter=" + publishedAfter;
		
		if(needCorrectDate) publishedBefore = getCorrectDateTimeForRequest(publishedBefore, true);
		if(publishedBefore!="") url_select += "&publishedBefore=" + publishedBefore;
		
		if(order!="") url_select += "&order=" + order; 
		
		if(pageToken!="") url_select += "&pageToken=" + pageToken; 
		

		if(maxResults!="") url_select += "&maxResults=" + maxResults; 
		else url_select += "&maxResults=20";
			
		return url_select;
	}
	
	public static String getCorrectDateTimeForRequest(String vnDate, Boolean isEndDate)
	{
		String result="";
		if(vnDate.trim()!="")
		{
			String []data = vnDate.split("-");
			if(data.length > 2){
				result +=  data[2] + "-"+data[1] + "-"+data[0];
				if(isEndDate) result += "T23:59:59.000Z";
				else result += "T00:00:00.000Z";
			}
		}
		return result;
	}
}
