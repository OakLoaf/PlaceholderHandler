package org.lushplugins.placeholderhandler;

import org.bukkit.plugin.java.JavaPlugin;

public final class PlaceholderHandler extends JavaPlugin {
    private static PlaceholderHandler plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        // Enable implementation
    }

    @Override
    public void onDisable() {
        // Disable implementation
    }

    public static PlaceholderHandler getInstance() {
        return plugin;
    }
}
