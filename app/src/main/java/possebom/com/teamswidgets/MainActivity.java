package possebom.com.teamswidgets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import possebom.com.teamswidgets.adapters.TeamsAdapter;
import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.event.UpdateEvent;
import possebom.com.teamswidgets.model.Team;


public class MainActivity extends BaseActivity {
    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Utils.getInstance(MainActivity.this, applicationList).uploadAll();
            TWController.INSTANCE.getDao().update(view.getContext());
        }
    };

    private RecyclerView mRecyclerView;
    private TeamsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TeamsAdapter(R.layout.card_teams, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        fabButton.setOnClickListener(fabClickListener);

        TWController.INSTANCE.getBus().register(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (event.getMessage() == null) {
            mAdapter.setTeamList(TWController.INSTANCE.getDao().getTeamList());
            Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
            fadeIn.setDuration(250);
            LayoutAnimationController layoutAnimationController = new LayoutAnimationController(fadeIn);
            mRecyclerView.setLayoutAnimation(layoutAnimationController);
            mRecyclerView.startLayoutAnimation();

            Picasso.with(this)
                    .load(TWController.INSTANCE.getDao().getTeamList().get(0).getImgUrl())
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.drawer_shadow)
                    .into(getFabImageView());
        }
    }

    public void animateActivity(Team team, View appIcon) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra("teamName", team.getName());

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(fabButton, "fab"), Pair.create(appIcon, "appIcon"));
        startActivity(i, transitionActivityOptions.toBundle());
    }
}