package org.lushplugins.placeholderhandler.parameter;

import org.lushplugins.placeholderhandler.placeholder.PlaceholderImpl;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderParser;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;
import org.lushplugins.placeholderhandler.placeholder.node.ParameterNode;
import org.lushplugins.placeholderhandler.placeholder.node.PlaceholderNode;
import org.lushplugins.placeholderhandler.stream.MutableStringStream;
import org.lushplugins.placeholderhandler.util.reflect.MethodCaller;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderMethod implements PlaceholderParser {
    private final MethodCaller.BoundMethodCaller caller;
    private final Map<String, PlaceholderParameter<?>> parameters;

    public PlaceholderMethod(MethodCaller.BoundMethodCaller caller, Map<String, PlaceholderParameter<?>> parameters) {
        this.caller = caller;
        this.parameters = parameters;
    }

    @Override
    public String parse(MutableStringStream input, PlaceholderImpl placeholder, PlaceholderContext context) {
        Map<String, Object> parameterArguments = new HashMap<>();
        for (PlaceholderNode node : placeholder.nodes()) {
            String parameter = input.readString();
            if (node instanceof ParameterNode<?> parameterNode) {
                parameterArguments.put(parameterNode.parameterName(), parameterNode.parse(parameter, context));
            }
        }

        Object[] orderedArgs = this.parameters.keySet().stream()
            .map(parameterName -> {
                if (parameterArguments.containsKey(parameterName)) {
                    return parameterArguments.get(parameterName);
                } else {
                    return this.parameters.get(parameterName);
                }
            })
            .toArray();

        return (String) this.caller.call(orderedArgs);
    }
}
