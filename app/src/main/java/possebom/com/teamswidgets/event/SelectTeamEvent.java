package possebom.com.teamswidgets.event;

/**
 * Created by alexandre on 02/11/14.
 */
public class SelectTeamEvent {

    private String name;

    public SelectTeamEvent(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
