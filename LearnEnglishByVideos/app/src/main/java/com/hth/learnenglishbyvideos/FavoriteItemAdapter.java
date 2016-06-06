package com.hth.learnenglishbyvideos;

import com.hth.data.Data;
import com.hth.data.ObjectFavorite;
import com.hth.learnenglishbyvideos.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FavoriteItemAdapter extends ArrayAdapter<ObjectFavorite> {
	private ArrayList<ObjectFavorite> data;
	private static LayoutInflater inflater = null;
	public Resources res;
	private Context context;

	public FavoriteItemAdapter(Context a, ArrayList<ObjectFavorite> d,
			Resources resLocal) {
		super(a, R.layout.favorite_item, R.id.title, d);
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
		ViewHolderFavorite viewHolder;
		TextView tvTitle;
		TextView tvNumberChapter;
		TextView tvTime;
		ImageButton removeButton;
		ImageView imgViewImage;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.favorite_item, null);
			viewHolder = new ViewHolderFavorite();

			viewHolder.title = tvTitle = (TextView) convertView
					.findViewById(R.id.title); // title
			
			
			viewHolder.countID = tvNumberChapter = (TextView) convertView
					.findViewById(R.id.countID);
			viewHolder.time = tvTime = (TextView) convertView
					.findViewById(R.id.time);
			viewHolder.removeButton = removeButton = (ImageButton) convertView
					.findViewById(R.id.removeFavorite);
			removeButton.setTag(data.get(position));
			viewHolder.removeButton.setFocusable(false);
			// removeButton.setVisibility(View.GONE);
			removeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					
					AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getContext()) 
			        //set message, title, and icon
			        .setTitle("Delete") 
			        .setMessage("Do you want to delete it?") 
			        .setIcon(R.drawable.delete)
			        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

			            public void onClick(DialogInterface dialog, int whichButton) { 
			                //your deleting code
			                Data.removeFavorite(getContext(),
									(ObjectFavorite) v.getTag());
							notifyDataSetChanged();
							dialog.dismiss();
			            }   

			        })
			        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                dialog.dismiss();
			            }
			        })
			        .create();
					myQuittingDialogBox.show();
				}
			});

			imgViewImage = (ImageView)convertView.findViewById(R.id.imgViewImage);
			imgViewImage.setBackgroundColor(0);
        	viewHolder.imgViewImage = imgViewImage;
        	
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderFavorite) convertView.getTag();
			tvTitle = viewHolder.title; // title
			tvNumberChapter = viewHolder.countID;
			tvTime = viewHolder.time;
			removeButton = viewHolder.removeButton;
			imgViewImage = viewHolder.imgViewImage;
		}
		ObjectFavorite objectFavorite = data.get(position);

		// Setting all values in listview
		tvTitle.setText(objectFavorite.getTitle());
		tvTitle.setTag(objectFavorite);
		tvTime.setText(objectFavorite.getTime());
		tvNumberChapter.setText("" + (position + 1));
		/*
		String contentImage = "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
      	      "content=\"text/html; charset=utf-8\"> </head><body><div style=\"background-color: rgba(10,10,10,0.5); " +
      	      "\"> "+"<img src='"+objectFavorite.getPath()+"'></img>"+ "</div> </body></html>";
		wvWebViewImage.loadDataWithBaseURL(null, contentImage, "text/html", "utf-8", null);
		*/
		Picasso.with(context)
				.load(objectFavorite.getPath())
				.into(imgViewImage);
		return convertView;
	}

}

class ViewHolderFavorite {
	TextView title;
	TextView time;
	TextView countID;
	ImageButton removeButton;
	ImageView imgViewImage;
}