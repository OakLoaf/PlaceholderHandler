package org.lushplugins.placeholderhandler.parameter;

import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;
import org.lushplugins.placeholderhandler.stream.MutableStringStream;

public record PlaceholderParameter<T, C extends PlaceholderContext>(String name, Class<?> type, ParameterProvider<T, C> provider) {

    public Object asObject(MutableStringStream input, C context) {
        return this.provider.collect((Class<T>) this.type, input.readString(), context);
    }
}
