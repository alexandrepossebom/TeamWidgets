package possebom.com.teamswidgets.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.squareup.otto.Subscribe;

import possebom.com.teamswidgets.R;
import possebom.com.teamswidgets.adapters.MatchesAdapter;
import possebom.com.teamswidgets.event.UpdateEvent;
import possebom.com.teamswidgets.model.Team;
import timber.log.Timber;

/**
 * Created by alexandre on 03/11/14.
 */
public class MatchesFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private MatchesAdapter mAdapter;
    private String teamName;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_matches, container, false);

        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.listMatches);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new MatchesAdapter();

        mRecyclerView.setAdapter(mAdapter);

        if (getArguments() != null) {
            teamName = getArguments().getString("teamName", "");
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");
        bus.register(this);
        dao.update();
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
        Timber.d("onPause");
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        Timber.d("onUpdate");
        final Team team = dao.getTeamByName(teamName);
        if ((event != null && event.getMessage() != null) || team == null) {
            setContentEmpty(true);
            return;
        }

        setContentShown(true);

        mAdapter.setTeam(team);
        Animation fadeIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        fadeIn.setDuration(250);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(fadeIn);
        mRecyclerView.setLayoutAnimation(layoutAnimationController);
        setTopPadding(mRecyclerView);
        mRecyclerView.setOnScrollListener(mScrollListener);
        toolBarUtils.setTitle(team.getName());
        dao.setDefaultTeamName(team.getName());
        mRecyclerView.startLayoutAnimation();
    }


}
