package org.lushplugins.placeholderhandler.parameter;

import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.annotation.AnnotationList;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;

@FunctionalInterface
public interface ParameterProvider<T> {
    T collect(Class<T> typeClass, String parameter, PlaceholderContext context);

    @FunctionalInterface
    interface Factory {
        ParameterProvider<?> create(Class<?> type, AnnotationList annotations, PlaceholderHandler instance);

        static ParameterProvider.Factory of(ParameterProvider<?> provider) {
            return (type, annotations, instance) -> provider;
        }
    }
}
