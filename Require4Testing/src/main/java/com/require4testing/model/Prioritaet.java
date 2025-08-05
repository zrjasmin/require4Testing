package com.require4testing.model;

public enum Prioritaet {
    NIEDRIG("Niedrig"),
    MITTEL("Mittel"),
    HOCH("Hoch");

    private final String displayName;

    Prioritaet(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}