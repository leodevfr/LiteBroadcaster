package org.plugins.litebroadcaster;

public class MessageConfig {

    private final String message;
    private final boolean onScreen;

    public MessageConfig(String message, boolean onScreen) {
        this.message = message;
        this.onScreen = onScreen;
    }

    public String getMessage() {
        return message;
    }

    public boolean isOnScreen() {
        return onScreen;
    }
}
