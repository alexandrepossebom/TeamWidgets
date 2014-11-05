package possebom.com.teamswidgets.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.squareup.otto.Bus;

import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.dao.DAO;

/**
 * Created by alexandre on 03/11/14.
 */
public abstract class BaseFragment extends Fragment {

    protected final DAO dao = TWController.INSTANCE.getDao();
    protected final Bus bus = TWController.INSTANCE.getBus();
    protected Context context;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }


}
