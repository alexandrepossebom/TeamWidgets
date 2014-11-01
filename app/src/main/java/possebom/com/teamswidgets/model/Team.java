package possebom.com.teamswidgets.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandre on 01/11/14.
 */
public class Team {

    private String name;
    private String imgUrl;
    private List<Match> matchList = new ArrayList<Match>();

    public List<Match> getMatchList() {
        return matchList;
    }

    public void setMatchList(final List<Match> matchList) {
        this.matchList = matchList;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(final String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }


}
