package com.possebom.teamswidgets.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.possebom.teamswidgets.controller.TWController;

import timber.log.Timber;

public class UpdateService extends Service {
    public UpdateService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TWController.INSTANCE.getDao().update();

        Timber.e("updateService preL");
        stopSelf(startId);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
