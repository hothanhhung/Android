package com.hth.data;

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
import android.util.Base64;

import com.hth.datmon.ImageProcess;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Photo {
  private Bitmap fullCroppedImage = null;
  private String fullPath;
  private boolean isCropped = false;
  private String name;
  private Bitmap smallImage = null;
  private Uri uri;

  public Photo() {
    this.name = "";
    this.fullPath = "";
  }

  public Photo(Uri paramUri, String name) {
    this.uri = paramUri;
    this.name = name;
    this.fullPath = uri.getPath();
  }

  public Photo(String paramString1, String paramString2, boolean paramBoolean) {
    this.name = paramString1;
    this.fullPath = paramString2;
  }

  public void cropImage(Bitmap paramBitmap) {
    this.fullCroppedImage = paramBitmap;
    this.isCropped = true;
    this.smallImage = null;
  }

  public String getFullPath() {
    return this.fullPath;
  }

  public Bitmap getFullPhoto() {
    Bitmap localBitmap = null;
    if (isCropped) {
      localBitmap = this.fullCroppedImage;
    } else {
      try {

          localBitmap = ImageProcess.decodeBitmapFromFile(getFullPath(), 600, 600);

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

  public Bitmap getSmallPhoto() {
    if (this.isCropped) {
      return this.fullCroppedImage;
    }
    if (smallImage == null) {
      try {
          smallImage = ImageProcess.decodeBitmapFromFile(getFullPath(), 80, 80);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return smallImage;
  }

  public Uri getUri() {
    return this.uri;
  }


  public void setFullPath(String paramString) {
    this.fullPath = paramString;
  }

  public void setName(String paramString) {
    this.name = paramString;
  }

  private static final int ROTATE_VALUE = 90;
  private int numberOfRotate = 0;

  public void rotate() {
    Bitmap bitmap = getSmallPhoto();
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

  public String GetDataInString()
  {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Bitmap bitmap = getSmallPhoto();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
    byte[] byteArray = byteArrayOutputStream .toByteArray();
    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
    return encoded;
  }
  public void free() {
    if (fullCroppedImage != null) {
      //fullCroppedImage.recycle();
      fullCroppedImage = null;
    }
    if (smallImage != null) {
      //smallImage.recycle();
      smallImage = null;
    }
  }

  static public Photo clone(Photo p) {
    if (p != null) {
      Photo photo = new Photo();
      photo.fullCroppedImage = p.fullCroppedImage == null? null : Bitmap.createBitmap(p.fullCroppedImage);
      photo.fullPath = p.fullPath;
      photo.isCropped = p.isCropped;
      photo.name = p.name;
      photo.smallImage = p.smallImage == null? null : Bitmap.createBitmap(p.smallImage);
      photo.uri = p.uri;
      return photo;
    }
    return null;
  }
}