package possebom.com.teamswidgets.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;

import com.squareup.otto.Bus;

import possebom.com.teamswidgets.R;
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

    protected void setTopPadding(View view) {
        int actionBarHeight = 0;

        final TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        final int paddingVertical = context.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        final int paddingTop = actionBarHeight + paddingVertical;
        final int paddingLeftRight = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);

        view.setPadding(paddingLeftRight, paddingTop, paddingLeftRight, paddingVertical);
    }


}
