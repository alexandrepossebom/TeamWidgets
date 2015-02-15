package com.possebom.teamswidgets.dashclock;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.possebom.teamswidgets.R;
import com.possebom.teamswidgets.controller.TWController;
import com.possebom.teamswidgets.model.Team;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends PreferenceFragment {

    protected static void setListPreferenceData(final ListPreference listPreference) {

        final List<CharSequence> teamNamesList = new ArrayList<>();
        for (Team team : TWController.INSTANCE.getDao().getTeamList()) {
            teamNamesList.add(team.getName());
        }

        final CharSequence[] charSequences = teamNamesList.toArray(new CharSequence[teamNamesList.size()]);

        listPreference.setEntries(charSequences);
        if (charSequences.length > 0)
            listPreference.setDefaultValue(teamNamesList.get(0));
        listPreference.setEntryValues(charSequences);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

        final ListPreference listPreference = (ListPreference) findPreference(TeamsWidgetsExtension.PREF_NAME);

        setListPreferenceData(listPreference);

        listPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setListPreferenceData(listPreference);
                return false;
            }
        });
    }
}

