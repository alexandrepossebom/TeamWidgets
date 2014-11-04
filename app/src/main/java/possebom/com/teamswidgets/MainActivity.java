package possebom.com.teamswidgets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.squareup.otto.Subscribe;

import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.event.SelectTeamEvent;
import possebom.com.teamswidgets.fragments.MatchsFragment;
import possebom.com.teamswidgets.fragments.TeamsFragment;
import timber.log.Timber;


public class MainActivity extends BaseActivity {


    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TWController.INSTANCE.getDao().update(view.getContext());
        }
    };


    private String curPage;
    private String curTeamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate");
        super.onCreate(savedInstanceState);
        fabButton.setOnClickListener(fabClickListener);
        TWController.INSTANCE.getBus().register(this);
        if (getSupportFragmentManager().getFragments() == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new TeamsFragment()).commit();
        } else {
            Timber.d("num frags : " + getSupportFragmentManager().getFragments().size());
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    public void selectTeam(String name) {
        Timber.d("num frags : " + getSupportFragmentManager().getFragments().size());
        Timber.d("selectTeam : " + name);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        curTeamName = name;
        Fragment fragment = new MatchsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("teamName", curTeamName);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }
    
}