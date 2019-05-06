package com.hunght.myfavoritesites;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.hunght.data.FavoriteSiteItem;
import com.hunght.dynamicgrid.BaseDynamicGridAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SiteItemAdapter extends BaseDynamicGridAdapter {
    public static final int TAG_VALUE = 10000;
    //private ArrayList<FavoriteSiteItem> data;
    private static LayoutInflater inflater=null;
    /*public Resources res;*/
    private Activity context;
    private Picasso picasso;
    public SiteItemAdapter( Activity a, ArrayList<FavoriteSiteItem> d, Resources resLocal, int numberOfColumns ) {
        super( a, d, numberOfColumns );
        context =a;
        /*data=d;
        res = resLocal;*/
        // Cache the LayoutInflate to avoid asking for a new one each time.
        inflater = LayoutInflater.from(a) ;
        //inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Picasso.Builder picassoBuilder = new Picasso.Builder(context);
        picassoBuilder.downloader(new OkHttpDownloader(context));
        picasso = picassoBuilder.build();
    }

    /*public int getCount() {
        return data.size();
    }

    public Object onRetainNonConfigurationInstance() {
        return data ;
    }*/
    /*public long getItemId(int position) {
        return position;
    }*/
    public ArrayList<FavoriteSiteItem> getDataItems() {
        ArrayList<FavoriteSiteItem> favoriteSiteItems = new ArrayList<>();
        for (Object item: getItems()) {
            favoriteSiteItems.add((FavoriteSiteItem) item);
        }
        return favoriteSiteItems;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        FavoriteSiteGridViewHolder viewHolder;
        TextView tvTitle;
        final ImageView imgImageView;
        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.favorite_site_item, null);
            viewHolder = new FavoriteSiteGridViewHolder();

            viewHolder.title = tvTitle = convertView.findViewById(R.id.title); // title
            imgImageView = convertView.findViewById(R.id.imgViewImage);
            imgImageView.setBackgroundColor(0);
            viewHolder.imgImageView = imgImageView;
            convertView.setTag(R.id.imgViewImage, viewHolder);
        }
        else{
            viewHolder = (FavoriteSiteGridViewHolder)convertView.getTag(R.id.imgViewImage);
            tvTitle = viewHolder.title;
            imgImageView = viewHolder.imgImageView;
        }
        final FavoriteSiteItem item = (FavoriteSiteItem)getItem(position);
        convertView.setTag(item);

        tvTitle.setText(item.getName());

        String faviconUrl = item.getFavicon();
        if(!faviconUrl.isEmpty()) {
            try {
                picasso.load(faviconUrl)
                        .into(imgImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                TextDrawable drawable = TextDrawable.builder()
                                        .buildRect(item.getAvatarName(), Color.RED);
                                imgImageView.setImageDrawable(drawable);
                            }
                        });
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return convertView;
    }

    class FavoriteSiteGridViewHolder
    {
        TextView title;
        ImageView imgImageView;
    }
}

