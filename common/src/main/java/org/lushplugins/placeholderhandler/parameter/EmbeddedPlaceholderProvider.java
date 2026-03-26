package org.lushplugins.placeholderhandler.parameter;

import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.annotation.AnnotationList;
import org.lushplugins.placeholderhandler.annotation.EmbeddedPlaceholder;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;

public class EmbeddedPlaceholderProvider<C extends PlaceholderContext> implements ParameterProvider.Factory<C> {

    @Override
    public ParameterProvider<?, C> create(Class<?> type, AnnotationList annotations, PlaceholderHandler<C> instance) {
        return annotations.contains(EmbeddedPlaceholder.class) ? new Provider<>() : null;
    }

    public static class Provider<C extends PlaceholderContext> implements ParameterProvider<String, C> {

        @Override
        public String collect(Class<String> typeClass, String parameter, C context) {
            return context.instance().parsePlaceholder(parameter);
        }
    }
}
