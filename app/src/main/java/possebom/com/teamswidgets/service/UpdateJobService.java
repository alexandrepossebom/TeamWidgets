package possebom.com.teamswidgets.service;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;
import possebom.com.teamswidgets.BaseApplication;
import possebom.com.teamswidgets.controller.TWController;
import timber.log.Timber;

public class UpdateJobService extends JobService {

    private static final String PREFS_NAME = "TeamPref";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Timber.d("onStartJob");
        TWController.INSTANCE.getDao().update();
        jobFinished(jobParameters,false);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String last = sharedPreferences.getString("last","");
        last = last + "\n" +android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss", new java.util.Date());;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("last", last);
        editor.apply();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Timber.d("onStopJob");
        return true;
    }
}
