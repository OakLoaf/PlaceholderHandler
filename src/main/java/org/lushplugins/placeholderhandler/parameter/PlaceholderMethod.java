package org.lushplugins.placeholderhandler.parameter;

import org.lushplugins.placeholderhandler.placeholder.PlaceholderParser;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;
import org.lushplugins.placeholderhandler.util.reflect.MethodCaller;

import java.util.Map;

public class PlaceholderMethod implements PlaceholderParser {
    private final MethodCaller.BoundMethodCaller caller;
    /**
     * Map of parameter name to parameter provider
     */
    private final Map<String, PlaceholderParameter<?>> parameterProviders;

    public PlaceholderMethod(MethodCaller.BoundMethodCaller caller, Map<String, PlaceholderParameter<?>> parameterProviders) {
        this.caller = caller;
        this.parameterProviders = parameterProviders;
    }

    @Override
    public String parse(PlaceholderContext context) {
        Object[] arguments = this.parameterProviders.values().stream()
            .map(provider -> provider.asObject(context))
            .toArray();

        return (String) this.caller.call(arguments);
    }
}
