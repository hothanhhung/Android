package com.hth.photopuzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhotosScreen extends Activity
{
  private static final int CROP_FROM_CAMERA = 2;
  private static final int PICK_FROM_CAMERA = 1;
  private static final int PICK_FROM_FILE = 3;
  private AdView adView;
  private Context context;
  private ImageView imgSelectedPhoto;

  private void doCrop()
  {
    final ArrayList cropOptions = new ArrayList();
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.setType("image/*");
    List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, 0);
    int size = resolveInfos.size();
    if (size != 0) {
      intent.setData(PhotoManager.getPhoto().getUri());
      intent.putExtra("outputX", 200);
      intent.putExtra("outputY", 200);
      intent.putExtra("aspectX", 1);
      intent.putExtra("aspectY", 1);
      intent.putExtra("scale", true);
      intent.putExtra("return-data", true);
      if (size != 1) {
        for (ResolveInfo res : resolveInfos) {
          MenuOption localMenuOption = new MenuOption();

          localMenuOption.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
          localMenuOption.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
          localMenuOption.appIntent = new Intent(intent);
          localMenuOption.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
          cropOptions.add(localMenuOption);
        }

        MenuOptionAdapter adapter = new MenuOptionAdapter(getApplicationContext(), cropOptions);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Crop App");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int item) {
            startActivityForResult(((MenuOption)cropOptions.get(item)).appIntent, CROP_FROM_CAMERA);
          }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
          @Override
          public void onCancel(DialogInterface dialog) {
          }
        });
        AlertDialog alert = builder.create();
        alert.show();
      } else {
        Intent i = new Intent(intent);
        ResolveInfo res = resolveInfos.get(0);
        i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
        startActivityForResult(i, CROP_FROM_CAMERA);
      }
    }
    else
    {
      Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
    }
  }

  private void startAdmobAds()
  {
    AdRequest localAdRequest = new AdRequest.Builder().build();
    this.adView = ((AdView)findViewById(R.id.adView));
    this.adView.loadAd(localAdRequest);
  }

  public void btGallery_Click(View paramView)
  {
    Intent localIntent = new Intent();
    localIntent.setType("image/*");
    localIntent.setAction("android.intent.action.GET_CONTENT");
    startActivityForResult(Intent.createChooser(localIntent, "Select Picture"), PICK_FROM_FILE);
  }

  File cameraPhotoFile = null;
  public void btCamera_Click(View paramView)
  {
    try {
      Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
      cameraPhotoFile = File.createTempFile("" + System.currentTimeMillis(), "jpg", MemoryHelper.getCacheFolder(context));
      //cameraIntent.putExtra("return-data", true);
      cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraPhotoFile));
      startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
    }catch(Exception ex){
      ex.printStackTrace();
      //display an error message
      String errorMessage = "Your device doesn't support capturing images!";
      Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
      toast.show();
    }
  }

  public void btPlayPhoto_Click(View paramView)
  {
    startActivity(new Intent(this, GameScreen.class));
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    if (resultCode == RESULT_OK)
    {
      switch (requestCode)
      {
      case PICK_FROM_CAMERA:
        if(cameraPhotoFile!=null) {
          PhotoManager.setPhoto(new Photo(Uri.fromFile(cameraPhotoFile)));
          doCrop();
        }
        break;
      case CROP_FROM_CAMERA:
        if(cameraPhotoFile!=null) {
          cameraPhotoFile.delete();
          cameraPhotoFile = null;
        }
        Bundle localBundle = data.getExtras();
        if (localBundle == null)
          break;
        PhotoManager.getPhoto().cropImage((Bitmap)localBundle.getParcelable("data"));
        break;
      case PICK_FROM_FILE:
        PhotoManager.setPhoto(new Photo(data.getData()));
        doCrop();
      }
      if ((this.imgSelectedPhoto != null) && (PhotoManager.getPhoto() != null))
        this.imgSelectedPhoto.setImageBitmap(PhotoManager.getPhoto().getSmallPhoto(this.context));
    }
  }

  ArrayList photoList;
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_photos_screen);
    this.context = this;
    this.imgSelectedPhoto = ((ImageView)findViewById(R.id.imgSelectedPhoto));
    photoList= PhotoManager.getAllPhotosInAssert(this);
    GridView grvPhotos = (GridView)findViewById(R.id.grvPhotos);
    grvPhotos.setAdapter(new PhotosGridViewAdapter(this, photoList));
    grvPhotos.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Photo localPhoto = (Photo) photoList.get(position);
        PhotoManager.setPhoto(localPhoto);
        if (localPhoto != null)
          PhotosScreen.this.imgSelectedPhoto.setImageBitmap(localPhoto.getSmallPhoto(PhotosScreen.this.context));
      }
    });
    if(photoList.size() > 0) {
      PhotoManager.setPhoto((Photo) photoList.get(0));
      this.imgSelectedPhoto.setImageBitmap(PhotoManager.getPhoto().getSmallPhoto(this.context));
    }
    startAdmobAds();
  }

  protected void onResume()
  {
    super.onResume();
    if ((this.imgSelectedPhoto != null) && (PhotoManager.getPhoto() != null))
      this.imgSelectedPhoto.setImageBitmap(PhotoManager.getPhoto().getSmallPhoto(this.context));
    System.gc();
  }
}