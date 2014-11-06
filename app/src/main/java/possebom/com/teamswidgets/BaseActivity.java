package possebom.com.teamswidgets;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.tundem.aboutlibraries.Libs;
import com.tundem.aboutlibraries.ui.LibsActivity;

import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.dao.DAO;
import possebom.com.teamswidgets.fragments.AboutFragment;
import possebom.com.teamswidgets.fragments.TeamsFragment;
import timber.log.Timber;

/**
 * Created by alexandre on 30/10/14.
 */
public abstract class BaseActivity extends ActionBarActivity {
    protected Toolbar toolbar;
    protected DAO dao;
    protected FragmentManager fragmentManager;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        fragmentManager = getSupportFragmentManager();

        Timber.tag("LifeCycles");
        Timber.d("Activity Created");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (mDrawerLayout != null) {
            setupDrawerActions();
        }


        dao = TWController.INSTANCE.getDao();
    }

    private void setupDrawerActions() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();

        // Handle different Drawer States :D
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        // Handle DrawerList
        mDrawerList = (LinearLayout) findViewById(R.id.drawerList);
        mDrawerList.findViewById(R.id.drawer_opensource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LibsActivity.class);

                i.putExtra(Libs.BUNDLE_FIELDS, Libs.toStringArray(R.string.class.getFields()));
                i.putExtra(Libs.BUNDLE_VERSION, false);
                i.putExtra(Libs.BUNDLE_LICENSE, true);
                i.putExtra(Libs.BUNDLE_TITLE, getString(R.string.drawer_opensource));
                i.putExtra(Libs.BUNDLE_THEME, R.style.AboutTheme);

                startActivity(i);
            }
        });

        mDrawerList.findViewById(R.id.drawer_teams).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new TeamsFragment()).commit();
                mDrawerLayout.closeDrawers();
            }
        });

        mDrawerList.findViewById(R.id.drawer_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new AboutFragment()).addToBackStack(null).commit();
                mDrawerLayout.closeDrawers();
            }
        });

        setIconDrawer(R.id.drawer_opensource_icon, Iconify.IconValue.fa_github);
        setIconDrawer(R.id.drawer_twitter_icon, Iconify.IconValue.fa_twitter);
        setIconDrawer(R.id.drawer_plus_icon, Iconify.IconValue.fa_google_plus_square);
        setIconDrawer(R.id.drawer_about_icon, Iconify.IconValue.fa_info_circle);
    }

    private void setIconDrawer(int resId, Iconify.IconValue iconId) {
        final ImageView imageView = (ImageView) mDrawerList.findViewById(resId);
        final Drawable drawable = new IconDrawable(this, iconId).colorRes(R.color.secondary).actionBarSize();
        imageView.setImageDrawable(drawable);
    }

    protected abstract int getLayoutResource();

}
