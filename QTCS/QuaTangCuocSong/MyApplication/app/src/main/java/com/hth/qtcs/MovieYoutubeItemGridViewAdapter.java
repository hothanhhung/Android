package com.hth.qtcs;

import java.util.ArrayList;

import com.hth.data.YouTubeVideo;
import com.hth.qtcs.R;
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

public class MovieYoutubeItemGridViewAdapter extends ArrayAdapter<YouTubeVideo> {
	 private ArrayList<YouTubeVideo> data;
    private static LayoutInflater inflater=null;
    public Resources res;
    private  Activity context;
    public MovieYoutubeItemGridViewAdapter( Activity a, ArrayList<YouTubeVideo> d, Resources resLocal ) {
        super( a, R.layout.movie_youtube_item, R.id.title, d );
        context =a;
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
    	MovieYoutubeGridViewHolder viewHolder;
        TextView tvTitle;
        TextView tvPublishTime;
        ImageView imgImageView;
    	if(convertView==null)
        {   
    		convertView = inflater.inflate(R.layout.movie_youtube_item_gridview, null);
        	viewHolder = new MovieYoutubeGridViewHolder();
 
        	viewHolder.title = tvTitle = (TextView)convertView.findViewById(R.id.title); // title
        	viewHolder.publishTime = tvPublishTime = (TextView)convertView.findViewById(R.id.publishTime);
            imgImageView = (ImageView)convertView.findViewById(R.id.imgImageView);
            imgImageView.setBackgroundColor(0);
        	viewHolder.imgImageView = imgImageView;
        	convertView.setTag(viewHolder);
        }
    	else{
            viewHolder = (MovieYoutubeGridViewHolder)convertView.getTag();
            tvTitle = viewHolder.title; // title
            tvPublishTime = viewHolder.publishTime;
            imgImageView = viewHolder.imgImageView;
        }
    	YouTubeVideo movie = data.get(position);
 
        // Setting all values in listview
        tvTitle.setText(movie.getTitle().toLowerCase());
        tvTitle.setTag(movie.getID());
        tvPublishTime.setText("Đăng lúc "+ movie.getPublishedTime());
       /*
        String contentImage = "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
        	      "content=\"text/html; charset=utf-8\"> </head><body><div style=\"background-color: rgba(10,10,10,0.5); " +
        	      "\"> "+"<img width='100%' src='"+movie.getThumbnailDefaut()+"'></img>"+ "</div> </body></html>";
        wvWebViewImage.loadDataWithBaseURL(null, contentImage, "text/html", "utf-8", null);
        */
        Picasso.with(context)
                .load(movie.getThumbnailDefaut())
                .into(imgImageView);

        return convertView;
    }

}

class MovieYoutubeGridViewHolder
{
    TextView title;
    TextView publishTime;
    ImageView imgImageView;
}