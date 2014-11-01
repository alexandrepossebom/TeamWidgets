package possebom.com.teamswidgets;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.model.Team;

public class DetailActivity extends ActionBarActivity {

    private static final int SCALE_DELAY = 30;
    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            UploadHelper.getInstance(DetailActivity.this, null).upload(appInfo);
        }
    };
    private Toolbar toolbar;
    private LinearLayout rowContainer;
    private Team team = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Utils.configureWindowEnterExitTransition(getWindow());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Handle Back Navigation :D
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Row Container
        rowContainer = (LinearLayout) findViewById(R.id.row_container);

        // Fab Button
        View fabButton = findViewById(R.id.fab_button);
        fabButton.setOnClickListener(fabClickListener);
//        Utils.configureFab(fabButton);


        for (int i = 1; i < rowContainer.getChildCount(); i++) {
            View rowView = rowContainer.getChildAt(i);
            rowView.animate().setStartDelay(100 + i * SCALE_DELAY).scaleX(1).scaleY(1);
        }
        String teamName = null;
        if (savedInstanceState != null) {
            teamName = savedInstanceState.getString("teamName");
        } else if (getIntent() != null && getIntent().getExtras() != null) {
            teamName = (String) getIntent().getExtras().get("teamName");
        }

        team = TWController.INSTANCE.getDao().getTeamByName(teamName);

        if (team != null) {
            //toolbar.setLogo(appInfo.getIcon());
            toolbar.setTitle(team.getName());

            View view = rowContainer.findViewById(R.id.row_name);
            fillRow(view, "Application Name", team.getName());

            Picasso.with(this)
                    .load(team.getImgUrl())
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.drawer_shadow)
                    .into(((ImageView) view.findViewById(R.id.appIcon)));

            Picasso.with(this)
                    .load(team.getImgUrl())
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.drawer_shadow)
                    .into((ImageView) fabButton);

            view = rowContainer.findViewById(R.id.row_package_name);
            fillRow(view, "Package Name", team.getName());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("teamName", team.getName());
        super.onSaveInstanceState(outState);
    }

    public void fillRow(View view, final String title, final String description) {
        TextView titleView = (TextView) view.findViewById(R.id.title);
        titleView.setText(title);

        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        descriptionView.setText(description);
    }

    @Override
    public void onBackPressed() {
        for (int i = rowContainer.getChildCount() - 1; i > 0; i--) {

            View rowView = rowContainer.getChildAt(i);
            ViewPropertyAnimator propertyAnimator = rowView.animate().setStartDelay((rowContainer.getChildCount() - 1 - i) * SCALE_DELAY)
                    .scaleX(0).scaleY(0);

            propertyAnimator.setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    } else {
                        finish();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
        }
    }
}