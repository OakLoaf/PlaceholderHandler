package org.lushplugins.placeholderhandler.annotation;

import org.jetbrains.annotations.NotNull;
import org.lushplugins.placeholderhandler.parameter.ParameterProvider;
import org.lushplugins.placeholderhandler.parameter.PlaceholderMethod;
import org.lushplugins.placeholderhandler.parameter.PlaceholderParameter;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderImpl;
import org.lushplugins.placeholderhandler.placeholder.node.LiteralNode;
import org.lushplugins.placeholderhandler.placeholder.node.PlaceholderNode;
import org.lushplugins.placeholderhandler.util.reflect.MethodCaller;
import org.lushplugins.placeholderhandler.util.reflect.MethodCallerFactory;
import org.lushplugins.placeholderhandler.util.reflect.Reflection;
import org.lushplugins.placeholderhandler.PlaceholderHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class AnnotationHandler {

    public static List<PlaceholderImpl> register(@NotNull Class<?> instanceClass, Object instance, PlaceholderHandler placeholderHandler) {
        List<List<PlaceholderNode>> baseNodes;
        Placeholder baseAnnotation = instanceClass.getAnnotation(Placeholder.class);
        if (baseAnnotation != null) {
            baseNodes = Arrays.stream(baseAnnotation.value())
                .map(path -> Arrays.stream(path.split("_"))
                    .map(parameter -> (PlaceholderNode) new LiteralNode(parameter))
                    .toList())
                .toList();
        } else {
            baseNodes = Collections.emptyList();
        }

        List<PlaceholderImpl> placeholders = new ArrayList<>();
        for (Method method : Reflection.getAllMethods(instanceClass)) {
            AnnotationList annotations = new AnnotationList(method);
            if (annotations.isEmpty()) {
                continue;
            }

            if (!containsPlaceholderAnnotation(annotations)) {
                continue;
            }

            MethodCaller.BoundMethodCaller caller;
            try {
                caller = MethodCallerFactory.defaultFactory()
                    .createFor(method)
                    .bindTo(instance);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            Map<String, PlaceholderParameter<?>> parameters = new LinkedHashMap<>();
            for (Parameter parameter : method.getParameters()) {
                String name = parameter.getName();

                ParameterProvider<?> provider = null;
                ParameterProvider.Factory providerFactory = placeholderHandler.getParameterProviderFactory(instanceClass);
                if (providerFactory != null) {
                    AnnotationList parameterAnnotations = new AnnotationList(parameter);
                    provider = providerFactory.create(instanceClass, parameterAnnotations, placeholderHandler);
                }

                if (provider == null) {
                    provider = placeholderHandler.getParameterProvider(instanceClass);
                }

                if (provider == null) {
                    throw new IllegalArgumentException("Invalid parameter type '%s' defined at method '%s' with parameter name '%s'"
                        .formatted(instanceClass.getSimpleName(), method.getName(), name));
                }

                parameters.put(name, new PlaceholderParameter<>(name, parameter.getType(), provider));
            }

            PlaceholderMethod placeholderMethod = new PlaceholderMethod(caller, parameters);

            if (annotations.contains(Placeholder.class)) {
                Placeholder methodAnnotation = annotations.get(Placeholder.class);
                for (String path : methodAnnotation.value()) {
                    List<PlaceholderNode> nodes = PlaceholderNode.create(path, parameters);
                    placeholders.add(new PlaceholderImpl(nodes, placeholderMethod));
                }
            }

            if (annotations.contains(SubPlaceholder.class)) {
                SubPlaceholder methodAnnotation = annotations.get(SubPlaceholder.class);
                for (String path : methodAnnotation.value()) {
                    List<PlaceholderNode> additionalNodes = PlaceholderNode.create(path, parameters);

                    for (List<PlaceholderNode> baseNode : baseNodes) {
                        List<PlaceholderNode> nodes = new ArrayList<>(baseNode);
                        nodes.addAll(additionalNodes);

                        placeholders.add(new PlaceholderImpl(nodes, placeholderMethod));
                    }
                }
            }
        }

        return placeholders;
    }

    private static boolean containsPlaceholderAnnotation(AnnotationList annotations) {
        return annotations.contains(Placeholder.class)
            || annotations.contains(SubPlaceholder.class);
    }
}
