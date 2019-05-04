package com.hunght.myfavoritesites;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunght.data.DataAccessor;
import com.hunght.data.FavoriteSiteItem;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SuggestionSiteItemAdapter extends BaseAdapter {
    public static final int TAG_VALUE = 10000;
    private ArrayList<FavoriteSiteItem> data;
    private static LayoutInflater inflater=null;
    /*public Resources res;*/
    private Activity context;
    private Picasso picasso;
    public SuggestionSiteItemAdapter(Activity a, ArrayList<FavoriteSiteItem> d) {
       // super( a, d, numberOfColumns );
        context =a;
        data=d;
        /*res = resLocal;*/
        // Cache the LayoutInflate to avoid asking for a new one each time.
        inflater = LayoutInflater.from(a) ;
        Picasso.Builder picassoBuilder = new Picasso.Builder(context);
        picassoBuilder.downloader(new OkHttpDownloader(context));
        picasso = picassoBuilder.build();
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int index) {
        return data.get(index);
    }

    public long getItemId(int position) {
        return position;
    }

    public ArrayList<FavoriteSiteItem> getDataItems() {
        ArrayList<FavoriteSiteItem> favoriteSiteItems = new ArrayList<>();
        for (Object item: data) {
            favoriteSiteItems.add((FavoriteSiteItem) item);
        }
        return favoriteSiteItems;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SuggestionSiteGridViewHolder viewHolder;
        TextView tvTitle;
        ImageView imgImageView;
        CheckBox cbSelectedItem;

        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.suggestion_sites_item, null);
            viewHolder = new SuggestionSiteGridViewHolder();

            viewHolder.title = tvTitle = convertView.findViewById(R.id.titleSuggestion); // title
            viewHolder.imgImageView = imgImageView = convertView.findViewById(R.id.imgViewImageSuggestion);
            viewHolder.cbSelectedItem = cbSelectedItem = convertView.findViewById(R.id.cbSelectedItemSuggestion);
            viewHolder.imgImageView.setBackgroundColor(0);
            convertView.setTag(R.id.imgViewImageSuggestion, viewHolder);
        }
        else{
            viewHolder = (SuggestionSiteGridViewHolder)convertView.getTag(R.id.imgViewImageSuggestion);
            tvTitle = viewHolder.title;
            imgImageView = viewHolder.imgImageView;
            cbSelectedItem = viewHolder.cbSelectedItem;
        }
        final FavoriteSiteItem item = (FavoriteSiteItem)getItem(position);
        convertView.setTag(item);

        tvTitle.setText(item.getName());

        if(DataAccessor.isInFavoriteSiteItems(item)){
            cbSelectedItem.setSelected(true);
        }else{
            cbSelectedItem.setSelected(false);
        }

        cbSelectedItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DataAccessor.updateFavoriteSiteItems(context, item);
                } else {
                    DataAccessor.removeFavoriteSiteItems(context, item);
                }
            }});

        String faviconUrl = item.getFavicon();
        if(!faviconUrl.isEmpty()) {
            try {
                picasso.load(faviconUrl)
                        .into(imgImageView);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return convertView;
    }

    class SuggestionSiteGridViewHolder
    {
        TextView title;
        ImageView imgImageView;
        CheckBox cbSelectedItem;
    }
}

