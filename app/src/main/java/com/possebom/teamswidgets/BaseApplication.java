package com.possebom.teamswidgets;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;

import com.possebom.teamswidgets.service.UpdateJobService;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import net.danlew.android.joda.JodaTimeAndroid;

import java.io.IOException;

import timber.log.Timber;

/**
 * Created by alexandre on 02/11/14.
 */
public class BaseApplication extends Application {

    private static Context context;
    private static Picasso picasso;

    public static Context getContext() {
        return context;
    }

    public static Picasso getPicasso() {
        return picasso;
    }

    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        context = getApplicationContext();
        OkHttpClient httpClient = new OkHttpClient();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final JobInfo job = new JobInfo.Builder(0, new ComponentName(context, UpdateJobService.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPeriodic(DateUtils.DAY_IN_MILLIS)
                    .setPersisted(true)
                    .build();

            final JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(job);
        }

        try {
            Cache responseCache = new Cache(context.getCacheDir(), 10 * 1024 * 1024);
            httpClient.setCache(responseCache);
        } catch (IOException e) {
            e.printStackTrace();
        }

        picasso = new Picasso.Builder(BaseApplication.getContext())
                .downloader(new OkHttpDownloader(httpClient))
                .build();
    }
}
