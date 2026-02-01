package com.health.record.util;

import com.health.record.entity.IdentifierType;

public final class IdentifierNormalizer {

    private IdentifierNormalizer() {}

    public static String normalize(IdentifierType type, String value) {
        if (value == null) return null;

        return switch (type) {
            case EMAIL -> value.trim().toLowerCase();
            case PHONE -> value.replaceAll("\\D", "");
            case ADDRESS -> value.trim(); // JSON string
        };
    }
}
