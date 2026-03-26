package org.lushplugins.placeholderhandler.hook;

import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderImpl;

import java.util.*;

public class HookRegistry<C extends PlaceholderContext> {
    private final List<PlaceholderHook<C>> hooks = new ArrayList<>();

    public void register(PlaceholderHook<C> hook) {
        this.hooks.add(hook);
    }

    public void registerAll(Collection<PlaceholderHook<C>> hooks) {
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

    public void registerPlaceholders(PlaceholderHandler<C> instance, Collection<PlaceholderImpl<C>> placeholders) {
        this.hooks.forEach(hook -> hook.register(instance, placeholders));
    }

    public void registerPlaceholder(PlaceholderHandler<C> instance, PlaceholderImpl<C> placeholder) {
        registerPlaceholders(instance, Collections.singletonList(placeholder));
    }
}
