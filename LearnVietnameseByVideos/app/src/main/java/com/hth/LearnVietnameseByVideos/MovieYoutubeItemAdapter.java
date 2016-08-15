package com.hth.LearnVietnameseByVideos;

import java.util.ArrayList;

import com.hth.data.YouTubeVideo;
import com.hth.LearnVietnameseByVideos.R;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieYoutubeItemAdapter extends ArrayAdapter<YouTubeVideo> {
	 private ArrayList<YouTubeVideo> data;
    private static LayoutInflater inflater=null;
    public Resources res;
    Activity context;

    public MovieYoutubeItemAdapter( Activity a, ArrayList<YouTubeVideo> d, Resources resLocal ) {
        super( a, R.layout.movie_youtube_item, R.id.title, d );
        context = a;
        data=d;
        res = resLocal;
        // Cache the LayoutInflate to avoid asking for a new one each time.
        inflater = LayoutInflater.from(a) ;
        //inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public int getCount() {
        return data.size();
    }

    public Object onRetainNonConfigurationInstance() {
        return data ;
    }
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
    	MovieYoutubeViewHolder viewHolder;
        TextView tvTitle;
        TextView tvPublishTime;
        ImageView imgViewImage;
    	if(convertView==null)
        {   
    		convertView = inflater.inflate(R.layout.movie_youtube_item, null);
        	viewHolder = new MovieYoutubeViewHolder();
 
        	viewHolder.title = tvTitle = (TextView)convertView.findViewById(R.id.title); // title
        	viewHolder.publishTime = tvPublishTime = (TextView)convertView.findViewById(R.id.publishTime);
            imgViewImage = (ImageView)convertView.findViewById(R.id.imgViewImage);
            imgViewImage.setBackgroundColor(0);
        	viewHolder.imgViewImage = imgViewImage;
        	convertView.setTag(viewHolder);
        }
    	else{
            viewHolder = (MovieYoutubeViewHolder)convertView.getTag();
            tvTitle = viewHolder.title; // title
            tvPublishTime = viewHolder.publishTime;
            imgViewImage = viewHolder.imgViewImage;
        }
    	YouTubeVideo movie = data.get(position);
 
        // Setting all values in listview
        tvTitle.setText(movie.getTitle());
        tvTitle.setTag(movie.getID());
        tvPublishTime.setText(movie.getPublishedTime());
        /*
        String contentImage = "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
        	      "content=\"text/html; charset=utf-8\"> </head><body><div style=\"background-color: rgba(10,10,10,0.5); " +
        	      "\"> "+"<img src='"+movie.getThumbnailDefaut()+"'></img>"+ "</div> </body></html>";
        imgViewImage.loadDataWithBaseURL(null, contentImage, "text/html", "utf-8", null);
        */
        Picasso.with(context)
                .load(movie.getThumbnailDefaut())
                .into(imgViewImage);
        return convertView;
    }

}

class MovieYoutubeViewHolder
{
    TextView title;
    TextView publishTime;
    ImageView imgViewImage;
}