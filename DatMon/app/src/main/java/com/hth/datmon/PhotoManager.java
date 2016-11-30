package com.hth.datmon;

import android.content.Context;

import android.graphics.Bitmap;

import com.hth.data.Photo;


public class PhotoManager
{
  private static Photo selectedPhoto;

  static
  {
    selectedPhoto = null;
  }

  public static boolean hasPhoto()
  {
    return selectedPhoto != null;
  }
  public static Photo getPhoto()
  {
    return selectedPhoto;
  }



  public static void setPhoto(Photo paramPhoto)
  {
    selectedPhoto = Photo.clone(paramPhoto);
  }

}