package io.github.andrew6rant.autoslabs;

import net.minecraft.util.StringIdentifiable;

public enum VerticalType implements StringIdentifiable {
    FALSE("false"),
    NORTH_SOUTH("north_south"),
    EAST_WEST("east_west");

    private final String name;

    private VerticalType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }
}
