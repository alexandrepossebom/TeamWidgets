package com.possebom.teamswidgets.event;

/**
 * Created by alexandre on 24/11/14.
 */
public class ErrorOnUpdateEvent {
    private final Throwable throwable;

    public ErrorOnUpdateEvent(final Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
