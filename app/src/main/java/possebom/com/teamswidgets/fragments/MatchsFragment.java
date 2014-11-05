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
import possebom.com.teamswidgets.interfaces.ToolBarUtils;
import possebom.com.teamswidgets.model.Team;
import timber.log.Timber;

/**
 * Created by alexandre on 03/11/14.
 */
public class MatchsFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private MatchesAdapter mAdapter;
    private Team team;
    private String teamName;
    private ToolBarUtils toolBarUtils;
    private boolean toobarIsHidden = false;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_matches, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.listMatches);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new MatchesAdapter();

        mRecyclerView.setAdapter(mAdapter);

        if (getArguments() != null) {
            teamName = getArguments().getString("teamName", "");
        }

//        showList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
        dao.update(getActivity());
        Timber.d("onResume");
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
        if (event.getMessage() != null) {
            return;
        }

        team = dao.getTeamByName(teamName);

        if (team != null) {
            mAdapter.setTeam(team);
            Animation fadeIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            fadeIn.setDuration(250);
            LayoutAnimationController layoutAnimationController = new LayoutAnimationController(fadeIn);
            mRecyclerView.setLayoutAnimation(layoutAnimationController);

            setTopPadding(mRecyclerView);

            toolBarUtils = (ToolBarUtils) getActivity();
            toolBarUtils.setTitle(team.getName());
            dao.setDefaultTeamName(team.getName());

            mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                    if (dy > 0 && !toobarIsHidden) {
                        toobarIsHidden = true;
                        toolBarUtils.hideToolBar();
                    } else if (dy < 0 && toobarIsHidden) {
                        toobarIsHidden = false;
                        toolBarUtils.showToolBar();
                    }
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            mRecyclerView.startLayoutAnimation();
        }
    }

}
