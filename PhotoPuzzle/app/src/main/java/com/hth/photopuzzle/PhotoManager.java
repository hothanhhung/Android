package com.hth.photopuzzle;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.Random;

public class PhotoManager
{
  private static final int MAX_PIECE = 16;
  private static Photo selectedPhoto;
  private static Bitmap selectedPhotoBitmap = null;

  static
  {
    selectedPhoto = null;
  }

  public static ArrayList<Photo> getAllPhotosInAssert(Context paramContext)
  {
    ArrayList localArrayList = new ArrayList();
    try
    {
      for (String str : paramContext.getAssets().list("photos"))
        localArrayList.add(new Photo(str, "photos/" + str, true));
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localArrayList;
  }

  public static Photo getPhoto()
  {
    return selectedPhoto;
  }

  public static Bitmap getPhotoBitmap()
  {
    return selectedPhotoBitmap;
  }

  public static Bitmap getPhotoPuzzle(int paramInt)
  {
    Bitmap localBitmap;
    if ((paramInt <= 16) && (selectedPhotoBitmap != null))
    {
      int k = selectedPhotoBitmap.getHeight();
      int i = selectedPhotoBitmap.getWidth();
      int j = i;
      if (k < i)
        j = k;
      int m;
      if (paramInt != -1)
      {
        i = j / 4;
        m = j / 4;
        j = m * ((paramInt - 1) % 4);
        k = i * ((paramInt - 1) / 4);
      }
      else
      {
        i = j;
        m = j;
        j = 0;
        k = 0;
      }
      localBitmap = Bitmap.createBitmap(selectedPhotoBitmap, j, k, m, i);
    }
    else
    {
      localBitmap = null;
    }
    return localBitmap;
  }

  public static void initialPhoto(Context paramContext, Photo paramPhoto)
  {
    setPhotoBitmap(paramPhoto.getFullPhoto(paramContext));
  }

  public static void initialPhoto(Context paramContext, boolean paramBoolean)
  {
    if ((selectedPhoto == null) || (paramBoolean))
    {
      Random localRandom = new Random();
      ArrayList localArrayList = getAllPhotosInAssert(paramContext);
      selectedPhoto = (Photo)localArrayList.get(localRandom.nextInt(localArrayList.size()));
    }
    setPhotoBitmap(selectedPhoto.getFullPhoto(paramContext));
  }

  public static void reset()
  {
    selectedPhoto = null;
  }

  public static void setPhoto(Photo paramPhoto)
  {
    selectedPhoto = paramPhoto;
  }

  private static void setPhotoBitmap(Bitmap paramBitmap)
  {
    selectedPhotoBitmap = paramBitmap;
  }
}