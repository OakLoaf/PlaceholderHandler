package org.lushplugins.placeholderhandler.parameter;

import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.annotation.AnnotationList;
import org.lushplugins.placeholderhandler.annotation.EmbeddedPlaceholder;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;

public class EmbeddedPlaceholderProvider implements ParameterProvider.Factory {

    @Override
    public ParameterProvider<?> create(Class<?> type, AnnotationList annotations, PlaceholderHandler instance) {
        return annotations.contains(EmbeddedPlaceholder.class) ? new Provider() : null;
    }

    public static class Provider implements ParameterProvider<String> {

        @Override
        public String collect(Class<String> typeClass, String parameter, PlaceholderContext context) {
            return context.instance().parsePlaceholder(parameter);
        }
    }
}
