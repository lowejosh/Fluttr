package com.example.charles.opencv.Gallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.charles.opencv.R;

public class GalleryAdapter extends BaseAdapter
{
    private Activity galleryActivity;
    private String[] path;
    private static LayoutInflater inflate = null;
    Bitmap bmp = null;

    public GalleryAdapter(Activity activity, String[] filePath) {
        galleryActivity = activity;
        path = filePath;
        inflate = (LayoutInflater) galleryActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount () {
        return path.length;
    }

    public Object getItem (int position) {
        return position;
    }

    public long getItemId (int position) {
        return position;
    }

    public View getView (int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = inflate.inflate(R.layout.gridview_item, null);
        ImageView image = view.findViewById(R.id.image);
        int width = 250;
        int height = 250;
        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path[position], bmpOptions);
        int cHeight = bmpOptions.outHeight;
        int cWidth = bmpOptions.outWidth;
        int sampleSize = 1;
        if (cHeight > height || cWidth > width)
        {
            if (cWidth > cHeight) {
                sampleSize = Math.round((float) cHeight / (float) height);
            }
            else {
                sampleSize = Math.round((float) cWidth / (float) width);
            }
        }
        bmpOptions.inSampleSize = sampleSize;
        bmpOptions.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path[position], bmpOptions);
        image.setImageBitmap(bmp);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        bmp = null;
        return view;
    }
}
