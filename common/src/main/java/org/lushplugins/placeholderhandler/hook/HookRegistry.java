package org.lushplugins.placeholderhandler.hook;

import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderImpl;

import java.util.*;

public class HookRegistry {
    private final List<PlaceholderHook> hooks = new ArrayList<>();

    public void register(PlaceholderHook hook) {
        this.hooks.add(hook);
    }

    public void registerAll(Collection<PlaceholderHook> hooks) {
        this.hooks.addAll(hooks);
    }

    // TODO
//    public void register(String pluginName, Callable<PlaceholderHook> callable) {
//        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
//        if (pluginManager.isPluginEnabled(pluginName)) {
//            try {
//                this.register(pluginName, callable.call());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    public void registerPlaceholders(PlaceholderHandler instance, Collection<PlaceholderImpl> placeholders) {
        this.hooks.forEach(hook -> hook.register(instance, placeholders));
    }

    public void registerPlaceholder(PlaceholderHandler instance, PlaceholderImpl placeholder) {
        registerPlaceholders(instance, Collections.singletonList(placeholder));
    }
}
