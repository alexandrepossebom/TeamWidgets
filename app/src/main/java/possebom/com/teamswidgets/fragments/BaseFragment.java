package possebom.com.teamswidgets.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.TypedValue;
import android.view.View;

import com.devspark.progressfragment.ProgressFragment;
import com.squareup.otto.Bus;

import possebom.com.teamswidgets.R;
import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.dao.DAO;
import possebom.com.teamswidgets.interfaces.ToolBarUtils;

/**
 * Created by alexandre on 03/11/14.
 */
public abstract class BaseFragment extends ProgressFragment {

    protected final DAO dao = TWController.INSTANCE.getDao();
    protected final Bus bus = TWController.INSTANCE.getBus();
    protected Context context;
    protected ToolBarUtils toolBarUtils;
    protected View mContentView;
    private boolean toolbarIsHidden = false;

    protected final OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
            if (dy > 0 && !toolbarIsHidden) {
                toolbarIsHidden = true;
                toolBarUtils.hideToolBar();
            } else if (dy < 0 && toolbarIsHidden) {
                toolbarIsHidden = false;
                toolBarUtils.showToolBar();
            }
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        try {
            toolBarUtils = (ToolBarUtils) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ToolBarUtils");
        }
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

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(mContentView);
        setEmptyText("lala");
    }

}
