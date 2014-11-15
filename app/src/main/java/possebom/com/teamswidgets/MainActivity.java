package possebom.com.teamswidgets;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import possebom.com.teamswidgets.adapters.TeamsAdapter;
import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.fragments.AboutFragment;
import possebom.com.teamswidgets.fragments.MatchesFragment;
import possebom.com.teamswidgets.fragments.TeamsFragment;
import possebom.com.teamswidgets.interfaces.ToolBarUtils;
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

        if (TWController.INSTANCE.getDefaultTeamName() == null) {
            showFragment(Frag.TEAMS, null);
        } else {
            onTeamSelected(TWController.INSTANCE.getDefaultTeamName());
        }

    }

    @Override
    protected void showFragment(final Frag frag, final Bundle bundle) {
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
    public void onTeamSelected(final String teamName) {
        final ImageView teamDrawer = (ImageView) findViewById(R.id.imageview_team_drawer);
        final ImageView drawerCover = (ImageView) findViewById(R.id.imageview_drawer_cover);

        Picasso.with(this)
                .load(dao.getTeamByName(teamName).getImgUrl())
                .placeholder(R.drawable.generic_team)
                .into(teamDrawer);

        Picasso.with(this)
                .load("http://possebom.com/widgets/soccer.jpg")
                .into(drawerCover);

        TWController.INSTANCE.setDefaultTeam(teamName);
        Timber.d("selectTeam : %s", teamName);
        Bundle bundle = new Bundle();
        bundle.putString("teamName", teamName);
        showFragment(Frag.MATCHES, bundle);
    }

    @Override
    public void hideToolBar() {
        Timber.d("hideToolBar");
        toolbar.animate().translationY(toolbar.getHeight() * -1);
        findViewById(R.id.drawerList).setPadding(0, 0, 0, 0);
    }

    @Override
    public void showToolBar() {
        Timber.d("showToolBar");
        toolbar.animate().translationY(0f);
        findViewById(R.id.drawerList).setPadding(0, toolbar.getHeight(), 0, 0);
    }

    @Override
    public void setTitle(final String title) {
        toolbar.setTitle(title);
    }
}