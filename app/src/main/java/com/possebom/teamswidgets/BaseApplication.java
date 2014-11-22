package com.possebom.teamswidgets;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.text.format.DateUtils;

import com.possebom.teamswidgets.service.UpdateJobService;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;
import timber.log.Timber;

/**
 * Created by alexandre on 02/11/14.
 */
public class BaseApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        context = getApplicationContext();

        final JobInfo job = new JobInfo.Builder(0, new ComponentName(context, UpdateJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(DateUtils.DAY_IN_MILLIS / 2)
                .setOverrideDeadline(DateUtils.DAY_IN_MILLIS)
                .setPersisted(true)
                .build();

        JobScheduler.getInstance(this).schedule(job);
    }
}
