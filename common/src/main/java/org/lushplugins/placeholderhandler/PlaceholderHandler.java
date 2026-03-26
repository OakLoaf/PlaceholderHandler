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

public class PlaceholderHandler<C extends PlaceholderContext> {
    private final List<PlaceholderImpl<C>> placeholders = new ArrayList<>();
    private final PlaceholderContext.Constructor<C> contextConstructor;
    private final Map<Class<?>, ParameterProvider.Factory<C>> parameterProviders;
    private final HookRegistry<C> hooks = new HookRegistry<>();

    public PlaceholderHandler(
        PlaceholderContext.Constructor<C> contextConstructor,
        Map<Class<?>, ParameterProvider.Factory<C>> parameterProviders,
        List<PlaceholderHook<C>> hooks
    ) {
        this.contextConstructor = contextConstructor;
        this.parameterProviders = parameterProviders;
        this.hooks.registerAll(hooks);
    }

    public List<PlaceholderImpl<C>> placeholders() {
        return placeholders;
    }

    public void register(Object... instances) {
        for (Object instance : instances) {
            if (instance instanceof PlaceholderImpl placeholder) {
                this.placeholders.add(placeholder);
                this.hooks.registerPlaceholder(this, placeholder);
                return;
            }

            List<PlaceholderImpl<C>> placeholders = AnnotationHandler.register(instance.getClass(), instance, this);
            this.placeholders.addAll(placeholders);
            this.hooks.registerPlaceholders(this, placeholders);
        }
    }

    public void register(String rawPlaceholder, PlaceholderParser<C> placeholder) {
        List<PlaceholderNode<C>> nodes = Arrays.stream(rawPlaceholder
                .substring(1, rawPlaceholder.length() - 1)
                .split("_"))
            .<PlaceholderNode<C>>map(LiteralNode::new)
            .toList();

        register(new PlaceholderImpl<>(nodes, placeholder));
    }

    /**
     * @param context the placeholder context to parse
     * @return the result of the parsed input
     */
    public @Nullable String parsePlaceholder(C context) {
        StringStream input = context.input();
        String identifier = input.peekUnquotedString();

        for (PlaceholderImpl<C> placeholder : this.placeholders) {
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
        return parsePlaceholder(contextConstructor.construct(rawPlaceholder, this));
    }

    public ParameterProvider.Factory<C> getParameterProvider(Class<?> type) {
        return this.parameterProviders.get(type);
    }

    public void registerHook(PlaceholderHook<C> hook) {
        this.hooks.register(hook);
    }

    public static <C extends PlaceholderContext> Builder<C> builder(PlaceholderContext.Constructor<C> contextConstructor) {
        return new Builder<>(contextConstructor)
            .registerGenericParameterProvider(String.class, (type, parameter, context) -> parameter)
            .registerGenericParameterProviderFactory(String.class, new EmbeddedPlaceholderProvider<>());
    }

    public static class Builder<C extends PlaceholderContext> {
        private final PlaceholderContext.Constructor<C> contextConstructor;
        private final Map<Class<?>, ParameterProvider.Factory<C>> parameterProviders = new HashMap<>();
        private final List<PlaceholderHook<C>> hooks = new ArrayList<>();

        private Builder(PlaceholderContext.Constructor<C> contextConstructor) {
            this.contextConstructor = contextConstructor;
        }

        public <T> Builder<C> registerGenericParameterProviderFactory(Class<T> type, ParameterProvider.Factory<C> provider) {
            this.parameterProviders.put(type, provider);
            return this;
        }

        public <T> Builder<C> registerGenericParameterProvider(Class<T> type, ParameterProvider<T, C> provider) {
            return registerGenericParameterProviderFactory(type, ParameterProvider.Factory.of(provider));
        }

        public <T> Builder<C> registerParameterProviderFactory(Class<T> type, ParameterProvider.Factory<C> provider) {
            this.parameterProviders.put(type, provider);
            return this;
        }

        public <T> Builder<C> registerParameterProvider(Class<T> type, ParameterProvider<T, C> provider) {
            return registerParameterProviderFactory(type, ParameterProvider.Factory.of(provider));
        }

        public Builder<C> registerHook(PlaceholderHook<C> hook) {
            this.hooks.add(hook);
            return this;
        }

        public PlaceholderHandler<?> build() {
            return new PlaceholderHandler<>(this.contextConstructor, this.parameterProviders, this.hooks);
        }
    }
}
