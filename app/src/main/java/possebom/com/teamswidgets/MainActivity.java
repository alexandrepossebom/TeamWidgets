package possebom.com.teamswidgets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.tundem.aboutlibraries.Libs;
import com.tundem.aboutlibraries.ui.LibsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import possebom.com.teamswidgets.adapters.TeamsAdapter;


public class MainActivity extends ActionBarActivity {
    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Utils.getInstance(MainActivity.this, applicationList).uploadAll();
        }
    };
    private List<String> applicationList = new ArrayList<String>();
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerList;
    private RecyclerView mRecyclerView;
    private TeamsAdapter mAdapter;
    private ImageView fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Handle Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Handle DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        // Handle ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();

        // Handle different Drawer States :D
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        // Handle DrawerList
        mDrawerList = (LinearLayout) findViewById(R.id.drawerList);


        mDrawerList.findViewById(R.id.drawer_opensource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create an intent with context and the Activity class
                Intent i = new Intent(getApplicationContext(), LibsActivity.class);
                //Pass the fields of your application to the lib so it can find all external lib information
                i.putExtra(Libs.BUNDLE_FIELDS, Libs.toStringArray(R.string.class.getFields()));

                //Display the library version (OPTIONAL)
                i.putExtra(Libs.BUNDLE_VERSION, false);
                //Display the library license (OPTIONAL
                i.putExtra(Libs.BUNDLE_LICENSE, true);

                //Set a title (OPTIONAL)
                i.putExtra(Libs.BUNDLE_TITLE, getString(R.string.drawer_opensource));

                //Pass your theme (OPTIONAL)
                i.putExtra(Libs.BUNDLE_THEME, R.style.AboutTheme);

                //start the activity
                startActivity(i);
            }
        });
        ((ImageView) mDrawerList.findViewById(R.id.drawer_opensource_icon)).setImageDrawable(new IconDrawable(this, Iconify.IconValue.fa_github).colorRes(R.color.secondary).actionBarSize());

        // Fab Button
        fabButton =(ImageView) findViewById(R.id.fab_button);
        fabButton.setImageDrawable(new IconDrawable(this, Iconify.IconValue.fa_location_arrow));
//        fabButton.setOnClickListener(new IconDrawable(this, Iconify.IconValue.fa_bank);
//        Utils.configureFab(fabButton);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TeamsAdapter(R.layout.card_teams, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        String[] countryArray = {"Australia", "China", "Italy", "Japan", "United Kingdom", "United States"};

        mAdapter.setApplications(Arrays.asList(countryArray));
    }

    @Override
    protected void onResume() {
        super.onResume();


        Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
        fadeIn.setDuration(250);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(fadeIn);
        mRecyclerView.setLayoutAnimation(layoutAnimationController);
        mRecyclerView.startLayoutAnimation();
    }

    public void animateActivity(View appIcon) {
//        Intent i = new Intent(this, DetailActivity.class);
//        i.putExtra("appInfo", appInfo.getComponentName());

//        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(fabButton, "fab"), Pair.create(appIcon, "appIcon"));
//        startActivity(i, transitionActivityOptions.toBundle());
    }

}