package com.possebom.teamswidgets;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.text.format.DateUtils;

import com.possebom.teamswidgets.service.UpdateJobService;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;
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
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        context = getApplicationContext();

        final JobInfo job = new JobInfo.Builder(0, new ComponentName(context, UpdateJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(DateUtils.DAY_IN_MILLIS)
                .setPersisted(true)
                .build();

        JobScheduler.getInstance(this).schedule(job);

        OkHttpClient httpClient = new OkHttpClient();

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
