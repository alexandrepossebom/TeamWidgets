package possebom.com.teamswidgets.service;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;
import possebom.com.teamswidgets.controller.TWController;
import timber.log.Timber;

public class UpdateJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Timber.d("onStartJob");
        TWController.INSTANCE.getDao().update();
        jobFinished(jobParameters,false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Timber.d("onStopJob");
        return true;
    }
}
