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
        final SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String last = sharedPreferences.getString("last","");

        String[] dates = last.split("\n");

        last = "";

        int count = dates.length > 24 ? 24 : dates.length;
        for(int i = 1; i< count;i++){
            last = last + dates[i] + "\n";
        }
        
        last = last + android.text.format.DateFormat.format("dd-MM-yyyy kk:mm:ss", new java.util.Date()) + "\n";

        sharedPreferences.edit().putString("last", last).apply();
        TWController.INSTANCE.getDao().update();
        jobFinished(jobParameters,true);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Timber.d("onStopJob");
        return true;
    }
}
