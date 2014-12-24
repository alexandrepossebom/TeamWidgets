package com.possebom.teamswidgets.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.possebom.teamswidgets.R;
import com.possebom.teamswidgets.adapters.TeamsAdapter;
import com.possebom.teamswidgets.controller.TWController;
import com.possebom.teamswidgets.dao.DAO;
import com.possebom.teamswidgets.event.ErrorOnUpdateEvent;
import com.possebom.teamswidgets.event.UpdateEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import timber.log.Timber;

/**
 * The configuration screen for the {@link WidgetLarge WidgetLarge} AppWidget.
 */
public class WidgetLargeConfigureActivity extends ActionBarActivity implements TeamsAdapter.OnTeamSelectedListener {

    private static final String PREFS_NAME = "com.possebom.teamswidgets.widgets.WidgetLarge";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    protected final DAO dao = TWController.INSTANCE.getDao();
    protected final Bus bus = TWController.INSTANCE.getBus();
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private RecyclerView recyclerView;
    private View mLoadingView;
    private int mShortAnimationDuration;

    private TeamsAdapter mAdapter;


    public WidgetLargeConfigureActivity() {
        super();
    }

    public static void saveTitlePref(final Context context, final int appWidgetId, final int id) {
        final SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, id);
        prefs.apply();
    }

    public static int loadTitlePref(final Context context, final int appWidgetId) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0);
    }

    public static void deleteTitlePref(final Context context, final int appWidgetId) {
        final SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.widget_small_configure);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLoadingView = findViewById(R.id.loading_spinner);

        // Initially hide the content view.
        recyclerView.setVisibility(View.GONE);

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ViewCompat.setElevation(toolbar, getResources().getDimension(R.dimen.elevation));
        if (toolbar != null) {
            try {
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle(R.string.configure_widget);
            } catch (Throwable t) {
                // WTF SAMSUNG!
            }
        }

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        // Find the widget id from the intent.
        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mAdapter = new TeamsAdapter(this, R.layout.card_widget_teams);
        recyclerView.setAdapter(mAdapter);


        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

    }

    @Override
    public void onTeamSelected(int teamId) {
        saveTitlePref(this, mAppWidgetId, teamId);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        WidgetLarge.updateAppWidget(this, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        final Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
        Timber.d("onPause");
    }

    @Subscribe
    public void onUpdateError(ErrorOnUpdateEvent event) {
        Timber.d("onUpdateError");
        crossfade();
        findViewById(R.id.textViewError).setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        Timber.d("onUpdate");
        mAdapter.setTeamList(TWController.INSTANCE.getDao().getTeamList());
        crossfade();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
        dao.update();
    }

    private void crossfade() {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        recyclerView.setAlpha(0f);
        recyclerView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        recyclerView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        mLoadingView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                });
    }
}





