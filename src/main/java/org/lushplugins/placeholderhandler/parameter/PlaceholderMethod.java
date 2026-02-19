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
import java.util.Set;

public class PlaceholderMethod implements PlaceholderParser {
    private final MethodCaller.BoundMethodCaller caller;
    private final Set<String> parameters;

    public PlaceholderMethod(MethodCaller.BoundMethodCaller caller, Set<String> parameters) {
        this.caller = caller;
        this.parameters = parameters;
    }

    @Override
    public String parse(MutableStringStream input, PlaceholderImpl placeholder, PlaceholderContext context) {
        Map<String, Object> arguments = new HashMap<>();
        for (PlaceholderNode node : placeholder.nodes()) {
            String parameter = input.readString();
            if (node instanceof ParameterNode<?> parameterNode) {
                arguments.put(parameterNode.parameterName(), parameterNode.parse(parameter, context));
            }
        }

        Object[] orderedArgs = this.parameters.stream()
            .map(arguments::get)
            .toArray();

        return (String) this.caller.call(orderedArgs);
    }
}
