package possebom.com.teamswidgets.model;

/**
 * Created by alexandre on 01/11/14.
 */
public class Match {
    private long timestamp;
    private Boolean home;
    private String transmission;
    private String opponent;
    private String league;
    private String place;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getHome() {
        return home;
    }

    public void setHome(final Boolean home) {
        this.home = home;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(final String transmission) {
        this.transmission = transmission;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(final String opponent) {
        this.opponent = opponent;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(final String league) {
        this.league = league;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(final String place) {
        this.place = place;
    }
}
