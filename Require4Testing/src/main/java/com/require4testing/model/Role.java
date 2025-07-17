package com.require4testing.model;

public enum Role {
	RE("Requirements Engineer"),
    TEST_MANAGER("Testmanager:in"),
    TESTER("Tester:in"),
    TEST_FALLERSTELLR("Testfallersteller:in");
	
	private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
