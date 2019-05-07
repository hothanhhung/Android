package com.hunght.myfavoritesites;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.hunght.data.DataAccessor;
import com.hunght.data.FavoriteSiteItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableSuggestionSiteItemAdapter extends BaseExpandableListAdapter {
    public static final int TAG_VALUE = 10000;
    private List<String> headers; // header titles
    private HashMap<String, List<FavoriteSiteItem>> data;
    private static LayoutInflater inflater=null;
    /*public Resources res;*/
    private Context context;
    private Picasso picasso;
    public ExpandableSuggestionSiteItemAdapter(Context context, List<String> headers,
                                               HashMap<String, List<FavoriteSiteItem>> data) {
       // super( a, d, numberOfColumns );
        this.context =context;
        this.data=data;
        this.headers = headers;
        /*res = resLocal;*/
        inflater = LayoutInflater.from(context) ;
        Picasso.Builder picassoBuilder = new Picasso.Builder(context);
        picassoBuilder.downloader(new OkHttpDownloader(context));
        picasso = picassoBuilder.build();
    }

    public Object getChild(int groupPosition, int childPosititon) {
        return this.data.get(this.headers.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.data.get(this.headers.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.headers.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.headers.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.suggestion_sites_header_group, null);
        }

        TextView lblListHeader = convertView
                .findViewById(R.id.tvheader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final SuggestionSiteGridViewHolder viewHolder;
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
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (SuggestionSiteGridViewHolder)convertView.getTag();
            tvTitle = viewHolder.title;
            imgImageView = viewHolder.imgImageView;
            cbSelectedItem = viewHolder.cbSelectedItem;
        }
        FavoriteSiteItem item = (FavoriteSiteItem)getChild(groupPosition, childPosition);
       // convertView.setTag(item);

        tvTitle.setText(item.getName());
        if(DataAccessor.isInFavoriteSiteItems(item)){
            cbSelectedItem.setChecked(true);
        }else{
            cbSelectedItem.setChecked(false);
        }

        cbSelectedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteSiteItem item = (FavoriteSiteItem)getChild(groupPosition, childPosition);
                if(((CompoundButton) v).isChecked()){
                    DataAccessor.updateFavoriteSiteItems(context, item);
                } else {
                    DataAccessor.removeFavoriteSiteItems(context, item);
                }
            }
        });
        /*cbSelectedItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FavoriteSiteItem item = (FavoriteSiteItem)getChild(groupPosition, childPosition);
                if (isChecked) {
                    DataAccessor.updateFavoriteSiteItems(context, item);
                } else {
                    DataAccessor.removeFavoriteSiteItems(context, item);
                }
            }});*/

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
                                FavoriteSiteItem item = (FavoriteSiteItem)getChild(groupPosition, childPosition);
                                TextDrawable drawable = TextDrawable.builder()
                                        .buildRect(item.getAvatarName(), Color.RED);
                                viewHolder.imgImageView.setImageDrawable(drawable);
                            }
                        });
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

