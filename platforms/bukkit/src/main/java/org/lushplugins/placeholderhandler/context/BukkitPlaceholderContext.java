package org.lushplugins.placeholderhandler.context;

import org.bukkit.entity.Player;
import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;

public class BukkitPlaceholderContext extends PlaceholderContext {
    private final Player player;

    public BukkitPlaceholderContext(String input, Player player, PlaceholderHandler instance) {
        super(input, instance);
        this.player = player;
    }

    public Player player() {
        return player;
    }
}
