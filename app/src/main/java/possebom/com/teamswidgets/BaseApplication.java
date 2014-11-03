package possebom.com.teamswidgets;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by alexandre on 02/11/14.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
