package possebom.com.teamswidgets;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

/**
 * Created by alexandre on 02/11/14.
 */
public class BaseApplication extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
