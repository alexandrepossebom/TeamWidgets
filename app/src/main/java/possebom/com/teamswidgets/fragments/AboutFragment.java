package possebom.com.teamswidgets.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import possebom.com.teamswidgets.R;


public class AboutFragment extends Fragment {

    private static final String PREFS_NAME = "TeamPref";

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.textview_about);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final String last = sharedPreferences.getString("last","");

        textView.setText("\n"+last);

        return rootView;
    }

}
