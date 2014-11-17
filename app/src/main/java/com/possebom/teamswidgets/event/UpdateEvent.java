package com.possebom.teamswidgets.event;

/**
 * Created by alexandre on 01/11/14.
 */
public class UpdateEvent {
    private final String message;

    public UpdateEvent(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
