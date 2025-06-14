package org.lushplugins.placeholderhandler.parameter;

import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;

public record PlaceholderParameter<T>(String name, Class<?> type, ParameterProvider<T> provider) {

    public Object asObject(PlaceholderContext context) {
        return this.provider.collect((Class<T>) this.type, context);
    }
}
