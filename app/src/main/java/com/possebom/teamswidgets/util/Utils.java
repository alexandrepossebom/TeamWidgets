package com.possebom.teamswidgets.util;

import java.net.URLEncoder;
import java.util.Locale;

/**
 * Created by alexandre on 11/02/15.
 */
public final class Utils {

    private Utils() {

    }

    public static String generateTeamLogoURL(final String teamName) {
        try {
            return String.format(Locale.getDefault(), "http://possebom.com/widgets/logos/%s.png", URLEncoder.encode(teamName, "UTF-8").replace("+", "%20"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
