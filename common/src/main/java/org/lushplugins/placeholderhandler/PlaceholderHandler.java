package org.lushplugins.placeholderhandler;

import org.jetbrains.annotations.Nullable;
import org.lushplugins.placeholderhandler.annotation.AnnotationHandler;
import org.lushplugins.placeholderhandler.hook.HookRegistry;
import org.lushplugins.placeholderhandler.hook.PlaceholderHook;
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
    private final List<PlaceholderImpl> placeholders = new ArrayList<>();
    private final Map<Class<?>, ParameterProvider.Factory> parameterProviders;
    private final HookRegistry hooks = new HookRegistry();

    public PlaceholderHandler(
        Map<Class<?>, ParameterProvider.Factory> parameterProviders,
        List<PlaceholderHook> hooks
    ) {
        this.parameterProviders = parameterProviders;
        this.hooks.registerAll(hooks);
    }

    public List<PlaceholderImpl> placeholders() {
        return placeholders;
    }

    public void register(Object... instances) {
        for (Object instance : instances) {
            if (instance instanceof PlaceholderImpl placeholder) {
                this.placeholders.add(placeholder);
                this.hooks.registerPlaceholder(this, placeholder);
                return;
            }

            List<PlaceholderImpl> placeholders = AnnotationHandler.register(instance.getClass(), instance, this);
            this.placeholders.addAll(placeholders);
            this.hooks.registerPlaceholders(this, placeholders);
        }
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
     * @param context the placeholder context to parse
     * @return the result of the parsed input
     */
    public @Nullable String parsePlaceholder(PlaceholderContext context) {
        StringStream input = context.input();
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

    /**
     * @param rawPlaceholder input in format '%placeholder_params%'
     * @return the result of the parsed input
     */
    public @Nullable String parsePlaceholder(String rawPlaceholder) {
        return parsePlaceholder(new PlaceholderContext(rawPlaceholder, this));
    }

    public ParameterProvider.Factory getParameterProvider(Class<?> type) {
        return this.parameterProviders.get(type);
    }

    public void registerHook(PlaceholderHook hook) {
        this.hooks.register(hook);
    }

    public static Builder builder() {
        return new Builder()
            .registerParameterProviderFactory(String.class, new EmbeddedPlaceholderProvider());
    }

    public static class Builder {
        private final Map<Class<?>, ParameterProvider.Factory> parameterProviders = new HashMap<>();
        private final List<PlaceholderHook> hooks = new ArrayList<>();

        private Builder() {}

        public <T> Builder registerParameterProviderFactory(Class<T> type, ParameterProvider.Factory provider) {
            this.parameterProviders.put(type, provider);
            return this;
        }

        public <T> Builder registerParameterProvider(Class<T> type, ParameterProvider<T> provider) {
            return registerParameterProviderFactory(type, ParameterProvider.Factory.of(provider));
        }

        public Builder registerHook(PlaceholderHook hook) {
            this.hooks.add(hook);
            return this;
        }

        public PlaceholderHandler build() {
            return new PlaceholderHandler(this.parameterProviders, this.hooks);
        }
    }
}
