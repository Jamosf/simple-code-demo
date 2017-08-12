package com.lewisen.goodnight.controller;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.lewisen.goodnight.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;

/**
 * 显示图片
 * 使用开源库universalimageloader
 * Created by Lewisen on 2015/9/12.
 */
public class DisplayImage {
    private ImageLoader imageLoader = null;
    private DisplayImageOptions options;

    public DisplayImage() {
        imageLoader = ImageLoader.getInstance();
    }

    /**
     * 显示图片
     *
     * @param imageView 控件
     * @param path      完整路径
     */
    public void display(ImageView imageView, String path) {
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.pic_blank)//设置下载期间的图片
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(100))
                .build();
        if ((path != null) && (path.length() > 10)) {
            try {
                imageLoader.displayImage(path, imageView, options);
                // Log.d("DEBUG", "imageLoader= " + MyServer.PICTURE_URL + image);
            } catch (Exception e) {
                // System.out.println("displayImage 错误");
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示圆角图片 imageview必须指定长宽大小
     */
    public void display(ImageView imageView, String path, int round) {
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.pic_blank)//设置下载期间的图片
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(100))
                .displayer(new RoundedBitmapDisplayer(round))//设置为圆角显示 前提imageview必须指定长宽大小
                .build();
        if ((path != null) && (path.length() > 10)) {
            try {
                imageLoader.displayImage(path, imageView, options);
                // Log.d("DEBUG", "imageLoader= " + MyServer.PICTURE_URL + image);
            } catch (Exception e) {
                // System.out.println("displayImage 错误");
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取缓存的图片文件
     *
     * @param path path
     * @return file
     */
    public File getImageCachePath(String path) {

        try {
            return imageLoader.getDiskCache().get(path);
        } catch (Exception e) {
            // System.out.println("displayImage 错误");
            e.printStackTrace();
        }
        return null;
    }


}
