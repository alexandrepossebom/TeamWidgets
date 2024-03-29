package com.possebom.teamswidgets.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.possebom.teamswidgets.MainActivity;
import com.possebom.teamswidgets.R;
import com.possebom.teamswidgets.adapters.TeamsAdapter;
import com.possebom.teamswidgets.controller.TWController;
import com.possebom.teamswidgets.dao.DAO;
import com.possebom.teamswidgets.event.ErrorOnUpdateEvent;
import com.possebom.teamswidgets.event.UpdateEvent;
import com.squareup.otto.Subscribe;

import timber.log.Timber;

/**
 * Created by alexandre on 03/11/14.
 */
public class TeamsFragment extends BaseFragment {

    private TeamsAdapter mAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_teams, container, false);

        final RecyclerView mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.listTeams);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        swipeRefreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DAO.INSTANCE.update(true);
            }
        });
        setSwipeScrollOffset();

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new TeamsAdapter((MainActivity) getActivity());

        mRecyclerView.setOnScrollListener(mScrollListener);
        mRecyclerView.setHasFixedSize(true);


        setTopPadding(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
        dao.update();
        toolBarUtils.showToolBar();
        Timber.d("onResume");
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
        setContentShown(true);
        setContentEmpty(true);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        Timber.d("onUpdate");
        setContentShown(true);
        swipeRefreshLayout.setRefreshing(false);
        mAdapter.setTeamList(TWController.INSTANCE.getDao().getTeamList());
    }

}
