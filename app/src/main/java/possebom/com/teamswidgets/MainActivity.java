package possebom.com.teamswidgets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import possebom.com.teamswidgets.adapters.TeamsAdapter;
import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.fragments.MatchsFragment;
import possebom.com.teamswidgets.fragments.TeamsFragment;
import possebom.com.teamswidgets.model.Team;
import timber.log.Timber;


public class MainActivity extends BaseActivity implements TeamsAdapter.OnTeamSelectedListener{

    private final View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TWController.INSTANCE.getDao().update(view.getContext());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate");
        super.onCreate(savedInstanceState);
        fabButton.setOnClickListener(fabClickListener);
        TWController.INSTANCE.getBus().register(this);
        if (fragmentManager.getFragments() == null) {
            fragmentManager.beginTransaction().add(R.id.fragment_container, new TeamsFragment()).commit();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void onTeamSelected(final Team team) {
        Timber.d("num frags : %s", getSupportFragmentManager().getFragments().size());
        Timber.d("selectTeam : %s", team.getName());
        Fragment fragment = new MatchsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("teamName", team.getName());
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }
}