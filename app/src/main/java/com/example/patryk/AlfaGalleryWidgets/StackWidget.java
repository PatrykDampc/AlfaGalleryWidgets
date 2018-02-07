package com.example.patryk.AlfaGalleryWidgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.patryk.AlfaGalleryWidgets.util.WidgetRemoteViewsService;


public class StackWidget extends AppWidgetProvider {
    public static final String PREVIEW_ACTION = "com.example.android.StackWidget.PREVIEW_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.StackWidget.EXTRA_ITEM";


    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(PREVIEW_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            String uriString = intent.getStringExtra("Uri_key");
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uriString)));
        }



        super.onReceive(context, intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stack_widget);
        Intent intent = new Intent(context, WidgetRemoteViewsService.class);

        Intent previewIntent = new Intent(context, GridWidget.class);
        previewIntent.setAction(GridWidget.PREVIEW_ACTION);
        previewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, previewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

        views.setRemoteAdapter(R.id.stack_view, intent);
        appWidgetManager.updateAppWidget(appWidgetId, views);


        updateAppWidget(context, appWidgetManager, appWidgetId);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stack_widget);
            Intent intent = new Intent(context, WidgetRemoteViewsService.class);

            Intent previewIntent = new Intent(context, GridWidget.class);
            previewIntent.setAction(GridWidget.PREVIEW_ACTION);
            previewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, previewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

            views.setRemoteAdapter(R.id.stack_view, intent);
            appWidgetManager.updateAppWidget(appWidgetId, views);


            updateAppWidget(context, appWidgetManager, appWidgetId);




        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the AlfaGalleryWidgets, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            StackWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first AlfaGalleryWidgets is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last AlfaGalleryWidgets is disabled
    }
}

