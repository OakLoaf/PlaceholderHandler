package org.lushplugins.placeholderhandler.parameter;

import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;
import org.lushplugins.placeholderhandler.stream.MutableStringStream;

public record PlaceholderParameter<T>(String name, Class<?> type, ParameterProvider<T> provider) {

    public Object asObject(MutableStringStream input, PlaceholderContext context) {
        return this.provider.collect((Class<T>) this.type, input.readString(), context);
    }
}
