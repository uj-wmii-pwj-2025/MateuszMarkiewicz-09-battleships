package org.example;

public enum HitState {
    MISS("pudło"),
    HIT("trafiony"),
    HIT_SUNK("trafiony zatopiony"),
    LAST_SUNK("ostatni zatopiony");

    private final String label;

    HitState(String state) {
        this.label = state;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}

