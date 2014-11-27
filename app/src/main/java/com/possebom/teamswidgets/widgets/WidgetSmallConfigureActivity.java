package com.possebom.teamswidgets.widgets;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.possebom.teamswidgets.R;
import com.possebom.teamswidgets.controller.TWController;
import com.possebom.teamswidgets.model.Team;


/**
 * The configuration screen for the {@link WidgetSmall WidgetSmall} AppWidget.
 */
public class WidgetSmallConfigureActivity extends Activity {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private static final String PREFS_NAME = "com.possebom.teamswidgets.WidgetSmall";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private Spinner spinner;

    public WidgetSmallConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.widget_small_configure);
        spinner = (Spinner) findViewById(R.id.spinner);

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        ArrayAdapter<Team> dataAdapter = new ArrayAdapter<Team>(this,android.R.layout.simple_spinner_item, TWController.INSTANCE.getDao().getTeamList());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WidgetSmallConfigureActivity.this;

            // When the button is clicked, store the string locally
            int id  = ((Team) spinner.getSelectedItem()).getId();
            saveTitlePref(context, mAppWidgetId, id);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            WidgetSmall.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    static void saveTitlePref(Context context, int appWidgetId, int id) {
        final SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, id);
        prefs.apply();
    }

    static int loadTitlePref(Context context, int appWidgetId) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int id = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0);
        return id;
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        final SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }
}



