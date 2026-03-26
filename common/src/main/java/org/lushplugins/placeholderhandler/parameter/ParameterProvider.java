package org.lushplugins.placeholderhandler.parameter;

import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.annotation.AnnotationList;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;

@FunctionalInterface
public interface ParameterProvider<T, C extends PlaceholderContext> {
    T collect(Class<T> typeClass, String parameter, C context);

    @FunctionalInterface
    interface Factory<C extends PlaceholderContext> {
        ParameterProvider<?, C> create(Class<?> type, AnnotationList annotations, PlaceholderHandler<C> instance);

        static <C extends PlaceholderContext> ParameterProvider.Factory<C> of(ParameterProvider<?, C> provider) {
            return (type, annotations, instance) -> provider;
        }
    }
}
