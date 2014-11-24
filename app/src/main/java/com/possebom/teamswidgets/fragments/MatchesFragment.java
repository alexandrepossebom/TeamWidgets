package com.possebom.teamswidgets.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.possebom.teamswidgets.R;
import com.possebom.teamswidgets.adapters.MatchesAdapter;
import com.possebom.teamswidgets.dao.DAO;
import com.possebom.teamswidgets.event.ErrorOnUpdateEvent;
import com.possebom.teamswidgets.event.UpdateEvent;
import com.possebom.teamswidgets.model.Team;
import com.squareup.otto.Subscribe;

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
        setTopPadding(mRecyclerView);

        swipeRefreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DAO.INSTANCE.update(true);
            }
        });
        setSwipeScrollOffset();

        mAdapter = new MatchesAdapter();
        mRecyclerView.setHasFixedSize(true);
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
    public void onUpdateError(ErrorOnUpdateEvent event) {
        Timber.d("onUpdateError");
        setContentEmpty(true);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        Timber.d("onUpdate");
        final Team team = dao.getTeamByName(teamName);
        if (team == null) {
            setContentEmpty(true);
            return;
        }
        setContentShown(true);
        swipeRefreshLayout.setRefreshing(false);

        mAdapter.setTeam(team);
        Animation fadeIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        fadeIn.setDuration(250);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(fadeIn);
        mRecyclerView.setLayoutAnimation(layoutAnimationController);
        mRecyclerView.setOnScrollListener(mScrollListener);
        toolBarUtils.setTitle(team.getName());
        dao.setDefaultTeamName(team.getName());
        mRecyclerView.startLayoutAnimation();
    }


}
