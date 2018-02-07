package com.example.patryk.AlfaGalleryWidgets.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.patryk.AlfaGalleryWidgets.GridWidget;
import com.example.patryk.AlfaGalleryWidgets.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private int imagesQuantity = 40;
    private Context ctx;
    private Cursor cursor;
    private ArrayList<Bitmap> photos = new ArrayList<>(imagesQuantity);
    private ArrayList<Uri> uris = new ArrayList<>(imagesQuantity);

    public WidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        ctx = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE,
        };
        cursor = ctx.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
        cursor.moveToFirst();
        for (int i = 1; i <= imagesQuantity; i++) {
            Bitmap bmp = BitmapFactory.decodeFile(cursor.getString(1));
            Uri imageUri= ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)));
            photos.add(bmp);
            uris.add(imageUri);
            cursor.moveToNext();
            Log.d(TAG, "loop iteration" + i );
        }
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        return imagesQuantity;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                cursor == null || !cursor.moveToPosition(position) || photos == null || photos.size() == 0 || position >= photos.size()) {
            return null;
        }
            RemoteViews views = new RemoteViews(ctx.getPackageName(), R.layout.widget_item);
            Bitmap img = BitmapResizer.resizeBitmap(250, 250, photos.get(position));
            views.setImageViewBitmap(R.id.widget_item, img);

        Bundle extras = new Bundle();
        extras.putInt(GridWidget.EXTRA_ITEM, position);
        extras.putString("Uri_key", String.valueOf(uris.get(position)));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return cursor.moveToPosition(position) ? cursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
