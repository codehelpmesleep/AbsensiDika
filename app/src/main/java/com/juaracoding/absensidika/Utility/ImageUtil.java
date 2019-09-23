package com.juaracoding.absensidika.Utility;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.juaracoding.absensidika.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;




public class ImageUtil {
	
	public static void displayImage(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.displayImage(path, view, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}
	
	public static void displayRoundImage(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.displayImage(path, view, ROUND_DISPLAY_IMAGE_OPTIONS, listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}
	
	public static void loadImage(String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.loadImage(path, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}
	
	//TODO Change default image
	private static final DisplayImageOptions.Builder DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.displayer(new FadeInBitmapDisplayer(300, true, false, false))
			.showImageForEmptyUri(R.drawable.ic_photo_camera)
			.showImageOnLoading(R.drawable.ic_photo_camera)
			.showImageOnFail(R.drawable.ic_photo_camera).cacheOnDisk(true)
			.cacheInMemory(true).bitmapConfig(Config.ARGB_8888);

	private static final DisplayImageOptions DEFAULT_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
			.build();
	private static final DisplayImageOptions ROUND_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
			.displayer(new RoundedBitmapDisplayer(30)).build();


    @SuppressLint("NewApi")
    public static boolean checkImageResource(Context ctx, ImageView imageView,
                                             int imageResource) {
        boolean result = false;

        if (ctx != null && imageView != null && imageView.getDrawable() != null) {
            Drawable.ConstantState constantState;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                constantState = ctx.getResources()
                        .getDrawable(imageResource, ctx.getTheme())
                        .getConstantState();
            } else {
                constantState = ctx.getResources().getDrawable(imageResource)
                        .getConstantState();
            }

            if (imageView.getDrawable().getConstantState() == constantState) {
                result = true;
            }
        }

        return result;
    }

    @SuppressLint("NewApi")
    public static boolean checkImageResource(Context ctx, ImageButton imageView,
                                             int imageResource) {
        boolean result = false;

        if (ctx != null && imageView != null && imageView.getDrawable() != null) {
            Drawable.ConstantState constantState;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                constantState = ctx.getResources()
                        .getDrawable(imageResource, ctx.getTheme())
                        .getConstantState();
            } else {
                constantState = ctx.getResources().getDrawable(imageResource)
                        .getConstantState();
            }

            if (imageView.getDrawable().getConstantState() == constantState) {
                result = true;
            }
        }

        return result;
    }


	public static Bitmap loadBitmap(String url)
	{
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try
		{
			URLConnection conn = new URL(url).openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is, 8192);
			bm = BitmapFactory.decodeStream(bis);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			if (bis != null)
			{
				try
				{
					bis.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return bm;
	}


}
