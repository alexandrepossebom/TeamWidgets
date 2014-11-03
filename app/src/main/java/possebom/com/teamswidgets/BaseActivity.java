package possebom.com.teamswidgets;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.mikpenz.iconics.Iconics;
import com.mikpenz.iconics.IconicsDrawable;
import com.mikpenz.iconics.typeface.FontAwesome;
import com.tundem.aboutlibraries.Libs;
import com.tundem.aboutlibraries.ui.LibsActivity;

import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.dao.DAO;
import timber.log.Timber;

/**
 * Created by alexandre on 30/10/14.
 */
public abstract class BaseActivity extends ActionBarActivity {
    protected Toolbar toolbar;
    protected View fabButton;
    protected DAO dao;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LinearLayout mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        Timber.tag("LifeCycles");
        Timber.d("Activity Created");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        fabButton = findViewById(R.id.fab_button);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (mDrawerLayout != null) {
            setupDrawerActions();
        }

        if (fabButton != null) {
            setupFabButton();
        }
        dao = TWController.INSTANCE.getDao();
    }

    protected ImageView getFabImageView() {
        ImageView imageView = null;
        if (fabButton instanceof ImageView) {
            imageView = (ImageView) fabButton;
        }
        return imageView;
    }

    private void setupFabButton() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabButton.setOutlineProvider(new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    int fabSize = view.getContext().getResources().getDimensionPixelSize(R.dimen.fab_size);
                    outline.setOval(0, 0, fabSize, fabSize);
                }
            });
        } else {
            getFabImageView().setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setupDrawerActions() {
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

        setIconDrawer(R.id.drawer_opensource_icon, Iconify.IconValue.fa_github);
        setIconDrawer(R.id.drawer_twitter_icon, Iconify.IconValue.fa_twitter);
        setIconDrawer(R.id.drawer_plus_icon, Iconify.IconValue.fa_google_plus_square);

        TextView tv = (TextView) mDrawerList.findViewById(R.id.tv);
//        new Iconics.IconicsBuilder().ctx(this).on(tv).build();
    }

    private void setIconDrawer(int resId, Iconify.IconValue iconId) {
        final ImageView imageView = (ImageView) mDrawerList.findViewById(resId);
        final Drawable drawable = new IconDrawable(this, iconId).colorRes(R.color.secondary).actionBarSize();
        imageView.setImageDrawable(drawable);
    }

    protected abstract int getLayoutResource();


}
