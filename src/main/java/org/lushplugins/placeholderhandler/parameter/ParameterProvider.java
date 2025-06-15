package org.lushplugins.placeholderhandler.parameter;

import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.annotation.AnnotationList;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;

import java.util.Map;

@FunctionalInterface
public interface ParameterProvider<T> {
    T collect(Class<T> typeClass, String parameter, PlaceholderContext context);

    static <T> Map.Entry<Class<T>, ParameterProvider<T>> forType(Class<T> typeClass, ParameterProvider<T> constructor) {
        return Map.entry(typeClass, constructor);
    }

    @FunctionalInterface
    interface Factory {
        ParameterProvider<?> create(Class<?> type, AnnotationList annotations, PlaceholderHandler instance);

        static <T> Map.Entry<Class<T>, ParameterProvider.Factory> forType(Class<T> typeClass, ParameterProvider.Factory constructor) {
            return Map.entry(typeClass, constructor);
        }
    }
}
