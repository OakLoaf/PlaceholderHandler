package org.lushplugins.placeholderhandler.context;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;

public class BukkitPlaceholderContext extends PlaceholderContext {
    private final Player player;

    public BukkitPlaceholderContext(String input, PlaceholderHandler<?> instance, @Nullable Player player) {
        super(input, instance);
        this.player = player;
    }

    public @Nullable Player player() {
        return player;
    }

    public static PlaceholderContext.Constructor<BukkitPlaceholderContext> constructor() {
        return (input, instance) -> new BukkitPlaceholderContext(input, instance, null);
    }
}
