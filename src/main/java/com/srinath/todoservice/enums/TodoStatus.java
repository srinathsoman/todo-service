package com.srinath.todoservice.enums;

public enum TodoStatus {

    NOT_DONE("not done"),
    DONE("done"),
    PAST_DUE("past due");

    private final String displayName;

    TodoStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
