package possebom.com.teamswidgets.util;

/**
 * Created by alexandre on 01/11/14.
 */
public class Log {
    private static final String TAG = "Possebom";

    public static void d(final String log) {
        android.util.Log.d(TAG, log == null ? "NULL" : log);
    }

    public static void i(final String log) {
        android.util.Log.i(TAG, log == null ? "NULL" : log);
    }
}
