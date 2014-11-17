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

import possebom.com.teamswidgets.MainActivity;
import possebom.com.teamswidgets.R;
import possebom.com.teamswidgets.adapters.TeamsAdapter;
import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.event.UpdateEvent;
import timber.log.Timber;

/**
 * Created by alexandre on 03/11/14.
 */
public class TeamsFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private TeamsAdapter mAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_teams, container, false);

        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.listTeams);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));


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
        if (event != null && event.getMessage() != null) {
            setContentEmpty(true);
            return;
        }

        setContentShown(true);

        mAdapter.setTeamList(TWController.INSTANCE.getDao().getTeamList());
        Animation fadeIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        fadeIn.setDuration(250);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(fadeIn);
        mRecyclerView.setLayoutAnimation(layoutAnimationController);
        mRecyclerView.startLayoutAnimation();

//            Picasso.with(this)
//                    .load(TWController.INSTANCE.getDao().getTeamList().get(0).getImgUrl())
//                    .placeholder(R.drawable.ic_launcher)
//                    .error(R.drawable.drawer_shadow)
//                    .into(getFabImageView());
    }

}
