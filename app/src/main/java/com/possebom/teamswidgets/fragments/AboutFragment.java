package com.possebom.teamswidgets.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.possebom.teamswidgets.R;
import com.possebom.teamswidgets.interfaces.ToolBarUtils;


public class AboutFragment extends Fragment {

    private static final String PREFS_NAME = "TeamPref";
    protected ToolBarUtils toolBarUtils;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        final TextView textView = (TextView) rootView.findViewById(R.id.textview_about);

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final String last = sharedPreferences.getString("last", "");

        textView.setText("\n" + last);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolBarUtils.showToolBar();
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        try {
            toolBarUtils = (ToolBarUtils) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ToolBarUtils");
        }
    }
}
