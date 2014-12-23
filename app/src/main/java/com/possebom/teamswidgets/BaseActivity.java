package com.possebom.teamswidgets;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikpenz.iconics.IconicsDrawable;
import com.mikpenz.iconics.typeface.FontAwesome;
import com.possebom.teamswidgets.controller.TWController;
import com.possebom.teamswidgets.dao.DAO;
import com.possebom.teamswidgets.model.Team;
import com.squareup.picasso.Picasso;
import com.tundem.aboutlibraries.Libs;
import com.tundem.aboutlibraries.ui.LibsActivity;

import hugo.weaving.DebugLog;
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

        fragmentManager = getFragmentManager();

        Timber.tag("LifeCycles");
        Timber.d("Activity Created");

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        ViewCompat.setElevation(toolbar, getResources().getDimension(R.dimen.elevation));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        if (toolbar != null) {
            try {
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } catch (Throwable t) {
                // WTF SAMSUNG!
            }
        }

        if (mDrawerLayout != null) {
            setupDrawerActions();
        }

        setupDrawerCover(null);

        dao = TWController.INSTANCE.getDao();
    }

    @DebugLog
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
                showFragment(Frag.TEAMS, null);
                mDrawerLayout.closeDrawers();
            }
        });

        mDrawerList.findViewById(R.id.drawer_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(Frag.ABOUT, null);
                mDrawerLayout.closeDrawers();
            }
        });

        setIconDrawer(R.id.drawer_opensource_icon, FontAwesome.Icon.faw_github);
        setIconDrawer(R.id.drawer_twitter_icon, FontAwesome.Icon.faw_twitter);
        setIconDrawer(R.id.drawer_plus_icon, FontAwesome.Icon.faw_google_plus);
        setIconDrawer(R.id.drawer_about_icon, FontAwesome.Icon.faw_info);
    }

    @DebugLog
    protected void setupDrawerCover(final Team team) {
        final ImageView drawerCover = (ImageView) findViewById(R.id.imageview_drawer_cover);
        Picasso.with(this)
                .load("http://possebom.com/widgets/soccer.jpg")
                .placeholder(android.R.color.holo_green_dark)
                .into(drawerCover);

        if (team == null) {
            return;
        }

        final ImageView teamDrawer = (ImageView) findViewById(R.id.imageview_team_drawer);
        final TextView textViewCoverTeamName = (TextView) findViewById(R.id.textview_cover_team_name);
        final TextView textViewCoverTeamDescription = (TextView) findViewById(R.id.textview_cover_team_description);

        Picasso.with(this)
                .load(team.getImgUrl())
                .placeholder(R.drawable.generic_team)
                .into(teamDrawer);

        textViewCoverTeamName.setText(team.getName());
        textViewCoverTeamDescription.setText(team.getName());
    }

    @DebugLog
    private void setIconDrawer(int resId, FontAwesome.Icon iconId) {
        final ImageView imageView = (ImageView) mDrawerList.findViewById(resId);
        final IconicsDrawable drawable = new IconicsDrawable(this, iconId).colorRes(R.color.secondary).sizeRes(R.dimen.widgetImageSize);
        imageView.setImageDrawable(drawable);
    }

    protected abstract void showFragment(Frag frag, Bundle bundle);

    protected abstract int getLayoutResource();

    protected enum Frag {
        ABOUT, TEAMS, MATCHES
    }

}
