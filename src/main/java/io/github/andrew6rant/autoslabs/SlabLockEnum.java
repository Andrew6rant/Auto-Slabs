package io.github.andrew6rant.autoslabs;

import net.minecraft.client.MinecraftClient;

public enum SlabLockEnum {
    DEFAULT_ALL,
    BOTTOM_SLAB,
    TOP_SLAB,
    NORTH_SLAB_VERTICAL,
    SOUTH_SLAB_VERTICAL,
    EAST_SLAB_VERTICAL,
    WEST_SLAB_VERTICAL;

    public static final SlabLockEnum[] POSITION_VALUES = values();

    public SlabLockEnum loopForward() {
        return POSITION_VALUES[(ordinal() + 1) % POSITION_VALUES.length];
    }

    public SlabLockEnum loopBackward() {
        return POSITION_VALUES[(ordinal() - 1  + POSITION_VALUES.length) % POSITION_VALUES.length];
    }

    public SlabLockEnum loop(MinecraftClient client) {
        if (client.options.sneakKey.isPressed()) {
            return loopBackward();
        } else {
            return loopForward();
        }
    }
}
