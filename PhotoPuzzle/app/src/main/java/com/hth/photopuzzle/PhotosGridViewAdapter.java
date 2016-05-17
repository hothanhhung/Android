package com.hth.photopuzzle;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import java.util.ArrayList;

public class PhotosGridViewAdapter extends BaseAdapter
{
  private Activity context;
  private ArrayList<Photo> data;

  public PhotosGridViewAdapter(Activity paramActivity, ArrayList<Photo> paramArrayList)
  {
    this.context = paramActivity;
    this.data = paramArrayList;
  }

  public int getCount()
  {
    return this.data.size();
  }

  public Object getItem(int paramInt)
  {
    return null;
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    AspectRatioImageView localAspectRatioImageView;
    if (paramView != null)
    {
      localAspectRatioImageView = (AspectRatioImageView)paramView;
    }
    else
    {
      localAspectRatioImageView = new AspectRatioImageView(this.context);
      localAspectRatioImageView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
      localAspectRatioImageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }
    Photo localPhoto = (Photo)this.data.get(paramInt);
    if (!localPhoto.isAssert())
      Picasso.with(this.context).load(Uri.parse(localPhoto.getFullPath())).into(localAspectRatioImageView);
    else
      localAspectRatioImageView.setImageBitmap(localPhoto.getSmallPhoto(this.context));
    return localAspectRatioImageView;
  }

  public Object onRetainNonConfigurationInstance()
  {
    return this.data;
  }
}
