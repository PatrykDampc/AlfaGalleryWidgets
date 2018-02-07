package com.example.patryk.AlfaGalleryWidgets.util;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by patryk on 03.02.2018.
 */

public class WidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
