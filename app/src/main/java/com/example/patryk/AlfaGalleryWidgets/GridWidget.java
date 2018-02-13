package com.example.patryk.AlfaGalleryWidgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.patryk.AlfaGalleryWidgets.util.WidgetRemoteViewsService;


public class GridWidget extends AppWidgetProvider {
    public static final String PREVIEW_ACTION = "com.example.android.GridWidget.PREVIEW_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.GridWidget.EXTRA_ITEM";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        boolean isThemeDefault = GridWidgetConfigure.loadPrefs(context, appWidgetId);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.grid_widget);
        if (!isThemeDefault) {
            views.setInt(R.id.widgetLayoutID, "setBackgroundColor", Color.argb(80, 255, 255, 255));
        }
        Intent intent = new Intent(context, WidgetRemoteViewsService.class);

        Intent previewIntent = new Intent(context, GridWidget.class);
        previewIntent.setAction(GridWidget.PREVIEW_ACTION);
        previewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent PreviewPendingIntent = PendingIntent.getBroadcast(context, 0, previewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.grid_view, PreviewPendingIntent);

        views.setRemoteAdapter(R.id.grid_view, intent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }



    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(PREVIEW_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            String uriString = intent.getStringExtra("Uri_key");
            context.startActivity(new Intent(Intent.ACTION_VIEW).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setDataAndType(Uri.parse(uriString), "image/*"));

        }


        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.grid_widget);
            Intent intent = new Intent(context, WidgetRemoteViewsService.class);

            Intent previewIntent = new Intent(context, GridWidget.class);
            previewIntent.setAction(GridWidget.PREVIEW_ACTION);
            previewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent PreviewPendingIntent = PendingIntent.getBroadcast(context, 0, previewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.grid_view, PreviewPendingIntent);

            views.setRemoteAdapter(R.id.grid_view, intent);

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

