package com.possebom.teamswidgets;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.possebom.teamswidgets.adapters.TeamsAdapter;
import com.possebom.teamswidgets.controller.TWController;
import com.possebom.teamswidgets.fragments.AboutFragment;
import com.possebom.teamswidgets.fragments.MatchesFragment;
import com.possebom.teamswidgets.fragments.TeamsFragment;
import com.possebom.teamswidgets.interfaces.ToolBarUtils;
import com.possebom.teamswidgets.model.Team;

import timber.log.Timber;


public class MainActivity extends BaseActivity implements TeamsAdapter.OnTeamSelectedListener, ToolBarUtils {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate");
        super.onCreate(savedInstanceState);
        TWController.INSTANCE.getBus().register(this);

        if (savedInstanceState != null) {
            return;
        }


        Bundle bundle = getIntent().getExtras();
        int teamId = 0;
        if (bundle != null) {
            teamId = bundle.getInt("teamId", 0);
            Timber.d("aqui !!! id = "+ teamId);
        }

        if (teamId != 0) {
            onTeamSelected(teamId);
        } else if (TWController.INSTANCE.getDefaultTeamId() == 0) {
            showFragment(Frag.TEAMS, null);
        } else {
            onTeamSelected(TWController.INSTANCE.getDefaultTeamId());
        }

    }

    @Override
    protected void showFragment(final Frag frag, final Bundle bundle) {
        setTitle(getString(R.string.app_name));
        Fragment fragment;
        switch (frag) {
            case ABOUT:
                fragment = new AboutFragment();
                break;
            case TEAMS:
                fragment = new TeamsFragment();
                break;
            case MATCHES:
                fragment = new MatchesFragment();
                break;
            default:
                Timber.d("Fragment not exists");
                return;
        }
        fragment.setArguments(bundle);
        Timber.d("num back stack : %d", fragmentManager.getBackStackEntryCount());
        if (fragmentManager.getFragments() != null) {
            Timber.d("num frags : %d", fragmentManager.getFragments().size());
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if (fragmentManager.getFragments() != null) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void onTeamSelected(final int teamId) {
        showToolBar();
        final Team team = dao.getTeamById(teamId);
        setupDrawerCover(team);
        TWController.INSTANCE.setDefaultTeam(teamId);
        Timber.d("selectTeam : %s", team.getName());
        Bundle bundle = new Bundle();
        bundle.putInt("teamId", teamId);
        showFragment(Frag.MATCHES, bundle);
    }

    @Override
    public void hideToolBar() {
        toolbar.animate().translationY(-toolbar.getHeight());
    }

    @Override
    public void showToolBar() {
        toolbar.animate().translationY(0f);
    }

    @Override
    public void setTitle(final String title) {
        toolbar.setTitle(title);
    }
}