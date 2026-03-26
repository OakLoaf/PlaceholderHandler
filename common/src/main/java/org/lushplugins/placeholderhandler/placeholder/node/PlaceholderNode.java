package org.lushplugins.placeholderhandler.placeholder.node;

import org.jetbrains.annotations.NotNull;
import org.lushplugins.placeholderhandler.parameter.PlaceholderParameter;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class PlaceholderNode<C extends PlaceholderContext> implements Comparable<PlaceholderNode<?>> {
    private final int priority;

    public PlaceholderNode(int priority) {
        this.priority = priority;
    }

    public int priority() {
        return priority;
    }

    public abstract boolean test(String parameter, C context);

    @Override
    public int compareTo(@NotNull PlaceholderNode other) {
        return Integer.compare(this.priority, other.priority());
    }

    public static <C extends PlaceholderContext> List<PlaceholderNode<C>> create(
        String path,
        Map<String, PlaceholderParameter<?, C>> parameters
    ) {
        if (path.startsWith("%") && path.endsWith("%")) {
            path = path.substring(1, path.length() - 1);
        }

        return Arrays.stream(path.split("_"))
            .<PlaceholderNode<C>>map(parameter -> {
                if (parameter.startsWith("<") && parameter.endsWith(">")) {
                    String parameterName = parameter.substring(1, parameter.length() - 1);
                    PlaceholderParameter<?, C> storedParameter = parameters.get(parameterName);
                    if (storedParameter != null) {
                        return new ParameterNode<>(storedParameter.type(), parameterName, storedParameter.provider());
                    } else {
                        throw new IllegalArgumentException("Could not find a parameter with name '%s'"
                            .formatted(parameterName));
                    }
                } else {
                    return new LiteralNode<>(parameter);
                }
            })
            .toList();
    }

    public static <C extends PlaceholderContext> List<PlaceholderNode<C>> create(String path) {
        return create(path, Collections.emptyMap());
    }
}
