package com.hth.photopuzzle;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.InputStream;

public class Photo {
  private Bitmap fullCroppedImage = null;
  private String fullPath;
  private boolean isAssert;
  private boolean isCropped = false;
  private String name;
  private Bitmap smallImage = null;
  private Uri uri;

  public Photo() {
    this.name = "";
    this.fullPath = "";
    this.isAssert = false;
  }

  public Photo(Uri paramUri) {
    this.uri = paramUri;
    this.name = "";
    this.fullPath = uri.getPath();
    this.isAssert = false;
  }

  public Photo(String paramString1, String paramString2, boolean paramBoolean) {
    this.name = paramString1;
    this.fullPath = paramString2;
    this.isAssert = paramBoolean;
  }

  public void cropImage(Bitmap paramBitmap) {
    this.fullCroppedImage = paramBitmap;
    this.isCropped = true;
    this.smallImage = null;
    this.isAssert = false;
  }

  public String getFullPath() {
    return this.fullPath;
  }

  public Bitmap getFullPhoto(Context paramContext) {
    Bitmap localBitmap = null;
    if (isCropped) {
      localBitmap = this.fullCroppedImage;
    } else {
      try {
        if (isAssert) {
          InputStream inputStream = paramContext.getAssets().open(getFullPath());
          localBitmap = ImageProcess.decodeBitmapFromStream(inputStream, 600, 600);
          inputStream.close();
        } else {
          localBitmap = ImageProcess.decodeBitmapFromFile(getFullPath(), 600, 600);
        }

        if(numberOfRotate%4 != 0)
        {
          Bitmap rotatedBitmap = rotateInDegree(localBitmap, numberOfRotate%4 * ROTATE_VALUE);
          localBitmap.recycle();
          localBitmap = rotatedBitmap;
        }
      } catch (Exception ex) {
        ex.printStackTrace();

      }
    }
    return localBitmap;
  }

  public String getName() {
    return this.name;
  }

  public Bitmap getSmallPhoto(Context paramContext) {
    if (this.isCropped) {
      return this.fullCroppedImage;
    }
    if (smallImage == null) {
      try {
        if (isAssert) {
          InputStream inputStream = paramContext.getAssets().open(getFullPath());
          smallImage = ImageProcess.decodeBitmapFromStream(inputStream, 80, 80);
          inputStream.close();
        } else {
          smallImage = ImageProcess.decodeBitmapFromFile(getFullPath(), 80, 80);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return smallImage;
  }

  public Uri getUri() {
    return this.uri;
  }

  public boolean isAssert() {
    return this.isAssert;
  }

  public void setFullPath(String paramString) {
    this.fullPath = paramString;
  }

  public void setIsAssert(boolean paramBoolean) {
    this.isAssert = paramBoolean;
  }

  public void setName(String paramString) {
    this.name = paramString;
  }

  private static final int ROTATE_VALUE = 90;
  private int numberOfRotate = 0;

  public void rotate(Context context) {
    Bitmap bitmap = getSmallPhoto(context);
    Bitmap rotated =rotateInDegree(bitmap, ROTATE_VALUE);
    if (isCropped) {
      fullCroppedImage.recycle();
      fullCroppedImage = rotated;
    } else {
      smallImage.recycle();
      smallImage = rotated;
    }
    numberOfRotate++;
  }

  private Bitmap rotateInDegree(Bitmap bitmap, int degree)
  {
    Matrix matrix = new Matrix();
    matrix.postRotate(degree);
    Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    return rotated;
  }

  public void free() {
    if (fullCroppedImage != null) {
      fullCroppedImage.recycle();
      fullCroppedImage = null;
    }
    if (smallImage != null) {
      smallImage.recycle();
      smallImage = null;
    }
  }

  static public Photo clone(Photo p) {
    if (p != null) {
      Photo photo = new Photo();
      photo.fullCroppedImage = p.fullCroppedImage == null? null : Bitmap.createBitmap(p.fullCroppedImage);
      photo.fullPath = p.fullPath;
      photo.isAssert = p.isAssert;
      photo.isCropped = p.isCropped;
      photo.name = p.name;
      photo.smallImage = p.smallImage == null? null : Bitmap.createBitmap(p.smallImage);
      photo.uri = p.uri;
      return photo;
    }
    return null;
  }
}