package com.moneymantra.storageshield;

import java.util.Locale;

public enum StorageShieldProfile {
    NORMAL,
    PERFORMANCE,
    EXTREME,
    OFF;

    public StorageShieldProfile next() {
        return switch (this) {
            case NORMAL -> PERFORMANCE;
            case PERFORMANCE -> EXTREME;
            case EXTREME -> OFF;
            case OFF -> NORMAL;
        };
    }

    public static StorageShieldProfile parse(String raw) {
        if (raw == null) {
            return PERFORMANCE;
        }
        try {
            return StorageShieldProfile.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return PERFORMANCE;
        }
    }

    public String displayName() {
        return switch (this) {
            case NORMAL -> "Normal";
            case PERFORMANCE -> "Performance";
            case EXTREME -> "Extreme";
            case OFF -> "Off";
        };
    }
}
