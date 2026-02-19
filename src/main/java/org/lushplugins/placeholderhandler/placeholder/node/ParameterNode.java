package org.lushplugins.placeholderhandler.placeholder.node;

import org.lushplugins.placeholderhandler.parameter.ParameterProvider;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;

public class ParameterNode<T> extends PlaceholderNode {
    private final Class<?> type;
    private final String parameterName;
    private final ParameterProvider<T> provider;

    public ParameterNode(
        Class<?> type,
        String parameterName,
        ParameterProvider<T> provider
    ) {
        super(1);
        this.type = type;
        this.parameterName = parameterName;
        this.provider = provider;
    }

    public String parameterName() {
        return parameterName;
    }

    @Override
    public boolean test(String parameter, PlaceholderContext context) {
        return this.provider.collect((Class<T>) this.type, parameter, context) != null;
    }

    public Object parse(String parameter, PlaceholderContext context) {
        return this.provider.collect((Class<T>) this.type, parameter, context);
    }
}
