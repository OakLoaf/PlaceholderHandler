package org.lushplugins.placeholderhandler.placeholder.node;

import org.lushplugins.placeholderhandler.parameter.ParameterProvider;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;

public class ParameterNode<T> extends PlaceholderNode {
    private final Class<T> type;
    private final ParameterProvider<T> provider;

    public ParameterNode(
        Class<T> type,
        ParameterProvider<T> provider
    ) {
        super(1);
        this.type = type;
        this.provider = provider;
    }

    @Override
    public boolean test(String parameter, PlaceholderContext context) {
        return this.provider.collect(this.type, context) != null;
    }
}
