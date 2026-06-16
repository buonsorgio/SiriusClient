package dev.sirius.client.module;

public enum Category {
    HUD("HUD"),
    VISUAL("Visual"),
    UTILITY("Utility");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
