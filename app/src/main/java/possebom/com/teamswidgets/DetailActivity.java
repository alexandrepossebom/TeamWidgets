package possebom.com.teamswidgets;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.squareup.picasso.Picasso;

import possebom.com.teamswidgets.adapters.MatchesAdapter;
import possebom.com.teamswidgets.model.Team;

public class DetailActivity extends BaseActivity {

    private final View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            UploadHelper.getInstance(DetailActivity.this, null).upload(appInfo);
        }
    };

    private Team team = null;
    private RecyclerView mRecyclerView;
    private MatchesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle Back Navigation :D
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.listMatches);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MatchesAdapter();
        mRecyclerView.setAdapter(mAdapter);
        fabButton.setOnClickListener(fabClickListener);

        String teamName = null;
        if (savedInstanceState != null) {
            teamName = savedInstanceState.getString("teamName");
        } else if (getIntent() != null && getIntent().getExtras() != null) {
            teamName = (String) getIntent().getExtras().get("teamName");
        }

        team = dao.getTeamByName(teamName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (team != null) {
            Picasso.with(this)
                    .load(team.getImgUrl())
                    .placeholder(R.drawable.generic_team)
                    .into(getFabImageView());
            mAdapter.setTeam(team);
            Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
            fadeIn.setDuration(250);
            LayoutAnimationController layoutAnimationController = new LayoutAnimationController(fadeIn);
            mRecyclerView.setLayoutAnimation(layoutAnimationController);
            mRecyclerView.startLayoutAnimation();
        }
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("teamName", team.getName());
        super.onSaveInstanceState(outState);
    }

}