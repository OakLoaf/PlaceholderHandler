package org.lushplugins.placeholderhandler;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.lushplugins.placeholderhandler.context.BukkitPlaceholderContext;
import org.lushplugins.placeholderhandler.hook.PlaceholderAPIHook;

public class BukkitPlaceholderHandler {

    public static PlaceholderHandler.Builder<BukkitPlaceholderContext> builder(JavaPlugin plugin) {
        return PlaceholderHandler.builder(BukkitPlaceholderContext.constructor())
            .registerParameterProvider(Player.class, (type, parameter, context) -> context.player())
            .registerHook(new PlaceholderAPIHook(plugin));
    }
}
