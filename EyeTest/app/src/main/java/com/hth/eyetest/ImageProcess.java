package com.hth.eyetest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import java.io.InputStream;

/**
 * Created by Lenovo on 4/22/2016.
 */
public class ImageProcess {

    public static Bitmap createVideoThumbnail(String filePath)
    {
        try {
            return ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Bitmap decodeBitmapFromFile(String pathFile, int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathFile, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(pathFile, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeBitmapFromStream(InputStream inputStream, int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            inputStream.mark(inputStream.available());
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            inputStream.reset();
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            //inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }
}
