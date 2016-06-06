package com.hth.learnenglishbyvideos;

import java.util.ArrayList;

import com.hth.data.YouTubeVideoDetail;
import com.hth.learnenglishbyvideos.R;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaylistYoutubeItemAdapter extends
		ArrayAdapter<YouTubeVideoDetail> {
	private ArrayList<YouTubeVideoDetail> data;
	private static LayoutInflater inflater = null;
	public Resources res;
	private Context context;

	public PlaylistYoutubeItemAdapter(Activity a,
			ArrayList<YouTubeVideoDetail> d, Resources resLocal) {
		super(a, R.layout.movie_youtube_item, R.id.title, d);
		context = a;
		data = d;
		res = resLocal;
		// Cache the LayoutInflate to avoid asking for a new one each time.
		inflater = LayoutInflater.from(a);
		// inflater =
		// (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object onRetainNonConfigurationInstance() {
		return data;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		PlaylistYoutubeViewHolder viewHolder;
		TextView tvTitle;
		TextView tvDescription;
		TextView tvPublishTime;
		ImageView imgViewImage;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.playlist_youtube_item, null);
			viewHolder = new PlaylistYoutubeViewHolder();

			viewHolder.title = tvTitle = (TextView) convertView
					.findViewById(R.id.title); // title
			viewHolder.description = tvDescription = (TextView) convertView
					.findViewById(R.id.description); // title
			viewHolder.publishTime = tvPublishTime = (TextView) convertView
					.findViewById(R.id.publishTime);
			imgViewImage = (ImageView)convertView.findViewById(R.id.imgViewImage);
			imgViewImage.setBackgroundColor(0);
			viewHolder.imgViewImage = imgViewImage;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (PlaylistYoutubeViewHolder) convertView.getTag();
			tvTitle = viewHolder.title; // title
			tvDescription = viewHolder.description; // title
			tvPublishTime = viewHolder.publishTime;
			imgViewImage = viewHolder.imgViewImage;
		}
		YouTubeVideoDetail movie = data.get(position);

		// Setting all values in listview
		tvTitle.setText(movie.getTitle());
		if (movie.getDescription().isEmpty()) {
			tvDescription.setVisibility(View.GONE);
		} else {
			tvDescription.setVisibility(View.VISIBLE);
			tvDescription.setText(movie.getDescription());
		}
		tvTitle.setTag(movie.getID());
		tvPublishTime.setText("   Videos: " + movie.getItemCount()
				+ "    Published: " + movie.getPublishedTime());
		/*
		String contentImage = "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" "
				+ "content=\"text/html; charset=utf-8\"> </head><body><div style=\"background-color: rgba(10,10,10,0.5); "
				+ "\"> "
				+ "<img width='100%' src='"
				+ movie.getThumbnailMedium()
				+ "'></img>"
				+ "</div> </body></html>";
		wvWebViewImage.loadDataWithBaseURL(null, contentImage, "text/html",
				"utf-8", null);
		*/

		Picasso.with(context)
				.load(movie.getThumbnailMedium())
				.into(imgViewImage);
		return convertView;
	}

}

class PlaylistYoutubeViewHolder {
	TextView title;
	TextView description;
	TextView publishTime;
	ImageView imgViewImage;
}