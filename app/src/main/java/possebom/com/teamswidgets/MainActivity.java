package possebom.com.teamswidgets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import possebom.com.teamswidgets.adapters.TeamsAdapter;
import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.fragments.MatchsFragment;
import possebom.com.teamswidgets.fragments.TeamsFragment;
import possebom.com.teamswidgets.interfaces.ToolBarUtils;
import possebom.com.teamswidgets.model.Team;
import timber.log.Timber;


public class MainActivity extends BaseActivity implements TeamsAdapter.OnTeamSelectedListener, ToolBarUtils {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate");
        super.onCreate(savedInstanceState);
        TWController.INSTANCE.getBus().register(this);
        if (fragmentManager.getFragments() == null && dao.getDefaultTeamName() == null) {
            fragmentManager.beginTransaction().add(R.id.fragment_container, new TeamsFragment()).commit();
        }else if(fragmentManager.getFragments() == null){
            onTeamSelected(dao.getDefaultTeamName(),false);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void onTeamSelected(final String teamName) {
        onTeamSelected(teamName,true);
    }

    public void onTeamSelected(final String teamName, boolean addToBackStack) {
        if(getSupportFragmentManager().getFragments() != null) {
            Timber.d("num frags : %d", getSupportFragmentManager().getFragments().size());
        }
        Timber.d("selectTeam : %s", teamName);
        Fragment fragment = new MatchsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("teamName", teamName);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment);
        if(addToBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
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