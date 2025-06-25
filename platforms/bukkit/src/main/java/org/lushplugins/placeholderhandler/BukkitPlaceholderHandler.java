package org.lushplugins.placeholderhandler;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.lushplugins.placeholderhandler.context.BukkitPlaceholderContext;
import org.lushplugins.placeholderhandler.hook.PlaceholderAPIHook;

public class BukkitPlaceholderHandler {

    public static PlaceholderHandler.Builder builder(JavaPlugin plugin) {
        return PlaceholderHandler.builder()
            .registerParameterProvider(Player.class, (type, parameter, context) -> ((BukkitPlaceholderContext) context).player())
            .registerHook(new PlaceholderAPIHook(plugin));
    }
}
