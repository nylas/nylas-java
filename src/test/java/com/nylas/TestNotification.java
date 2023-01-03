package com.nylas;

public class TestNotification extends Event.Notification {

    /**
     * For deserialization only
     *
     * @param type
     */
    public TestNotification(String type) {
        super(type);
    }
}
