package org.lushplugins.placeholderhandler;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.placeholderhandler.annotation.AnnotationHandler;
import org.lushplugins.placeholderhandler.hook.PlaceholderAPIHook;
import org.lushplugins.placeholderhandler.parameter.EmbeddedPlaceholderProvider;
import org.lushplugins.placeholderhandler.parameter.ParameterProvider;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderImpl;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderParser;
import org.lushplugins.placeholderhandler.placeholder.node.LiteralNode;
import org.lushplugins.placeholderhandler.placeholder.node.PlaceholderNode;
import org.lushplugins.placeholderhandler.stream.StringStream;

import java.util.*;

public final class PlaceholderHandler {
    private final JavaPlugin plugin;
    private final List<PlaceholderImpl> placeholders = new ArrayList<>();
    private final Map<Class<?>, ParameterProvider<?>> parameterProviders;
    private final Map<Class<?>, ParameterProvider.Factory> parameterProviderFactories;

    public PlaceholderHandler(
        JavaPlugin plugin,
        Map<Class<?>, ParameterProvider<?>> parameterProviders,
        Map<Class<?>, ParameterProvider.Factory> parameterProviderFactories
    ) {
        this.plugin = plugin;
        this.parameterProviders = parameterProviders;
        this.parameterProviderFactories = parameterProviderFactories;

        PluginManager pluginManager = plugin.getServer().getPluginManager();
        if (pluginManager.isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPIHook.register(this);
        }
    }

    public JavaPlugin plugin() {
        return plugin;
    }

    public List<PlaceholderImpl> placeholders() {
        return placeholders;
    }

    public void register(Object instance) {
        if (instance instanceof PlaceholderImpl placeholder) {
            this.placeholders.add(placeholder);
            return;
        }

        List<PlaceholderImpl> placeholders = AnnotationHandler.register(instance.getClass(), instance, this);
        this.placeholders.addAll(placeholders);
    }

    public void register(String rawPlaceholder, PlaceholderParser placeholder) {
        List<PlaceholderNode> nodes = Arrays.stream(rawPlaceholder
                .substring(1, rawPlaceholder.length() - 1)
                .split("_"))
            .map(parameter -> (PlaceholderNode) new LiteralNode(parameter))
            .toList();

        register(new PlaceholderImpl(nodes, placeholder));
    }

    /**
     * @param rawPlaceholder input in format '%placeholder_params%'
     * @param player the player to parse the input for
     * @return the result of the parsed input
     */
    public @Nullable String parsePlaceholder(String rawPlaceholder, @Nullable Player player) {
        StringStream input = StringStream.create(rawPlaceholder
            .substring(1, rawPlaceholder.length() - 1));
        PlaceholderContext context = new PlaceholderContext(input, player, this);

        String identifier = input.peekUnquotedString();
        for (PlaceholderImpl placeholder : this.placeholders) {
            if (!placeholder.firstNode().name().equals(identifier)) {
                continue;
            }

            if (placeholder.isValid(input.toMutableCopy(), context)) {
                return placeholder.parse(input.toMutableCopy(), context);
            }
        }

        return null;
    }

    public ParameterProvider<?> getParameterProvider(Class<?> type) {
        return this.parameterProviders.get(type);
    }

    public ParameterProvider.Factory getParameterProviderFactory(Class<?> type) {
        return this.parameterProviderFactories.get(type);
    }

    public static Builder builder(JavaPlugin plugin) {
        return new Builder(plugin);
    }

    public static class Builder {
        private final JavaPlugin plugin;
        private final Map<Class<?>, ParameterProvider<?>> parameterProviders = new HashMap<>(Map.ofEntries(
            ParameterProvider.forType(Player.class, (type, parameter, context) -> context.player())
        ));
        private final Map<Class<?>, ParameterProvider.Factory> parameterProviderFactories = new HashMap<>(Map.ofEntries(
            ParameterProvider.Factory.forType(String.class, new EmbeddedPlaceholderProvider())
        ));

        private Builder(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        public <T> Builder registerParameterProvider(Class<T> type, ParameterProvider<T> provider) {
            this.parameterProviders.put(type, provider);
            return this;
        }

        public Builder registerParameterProviderFactory(Class<?> type, ParameterProvider.Factory provider) {
            this.parameterProviderFactories.put(type, provider);
            return this;
        }

        public PlaceholderHandler build() {
            return new PlaceholderHandler(this.plugin, this.parameterProviders, this.parameterProviderFactories);
        }
    }
}
