package org.lushplugins.placeholderhandler.hook;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class HookRegistry {
    private final Map<String, PlaceholderHook> hooks = new HashMap<>();

    public void register(String hookName, PlaceholderHook hook) {
        this.hooks.put(hookName, hook);
    }

    public void register(String pluginName, Callable<PlaceholderHook> callable) {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        if (pluginManager.isPluginEnabled(pluginName)) {
            try {
                this.register(pluginName, callable.call());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void registerPlaceholders(PlaceholderHandler instance, Collection<PlaceholderImpl> placeholders) {
        this.hooks.values().forEach(hook -> hook.register(instance, placeholders));
    }

    public void registerPlaceholder(PlaceholderHandler instance, PlaceholderImpl placeholder) {
        registerPlaceholders(instance, Collections.singletonList(placeholder));
    }
}
