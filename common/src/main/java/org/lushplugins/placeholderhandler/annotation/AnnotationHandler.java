package org.lushplugins.placeholderhandler.annotation;

import org.jetbrains.annotations.NotNull;
import org.lushplugins.placeholderhandler.parameter.ParameterProvider;
import org.lushplugins.placeholderhandler.parameter.PlaceholderMethod;
import org.lushplugins.placeholderhandler.parameter.PlaceholderParameter;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;
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

    public static <C extends PlaceholderContext> List<PlaceholderImpl<C>> register(@NotNull Class<?> instanceClass, Object instance, PlaceholderHandler<C> placeholderHandler) {
        List<List<PlaceholderNode<C>>> baseNodes;
        Placeholder baseAnnotation = instanceClass.getAnnotation(Placeholder.class);
        if (baseAnnotation != null) {
            baseNodes = Arrays.stream(baseAnnotation.value())
                .map(path -> Arrays.stream(path.split("_"))
                    .<PlaceholderNode<C>>map(LiteralNode::new)
                    .toList())
                .toList();
        } else {
            baseNodes = Collections.emptyList();
        }

        List<PlaceholderImpl<C>> placeholders = new ArrayList<>();
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

            Map<String, PlaceholderParameter<?, C>> parameters = new LinkedHashMap<>();
            for (Parameter parameter : method.getParameters()) {
                String name = parameter.getName();
                Class<?> parameterClass = parameter.getType();

                ParameterProvider<?, C> provider = null;
                ParameterProvider.Factory<C> providerFactory = placeholderHandler.getParameterProvider(parameterClass);
                if (providerFactory != null) {
                    AnnotationList parameterAnnotations = new AnnotationList(parameter);
                    provider = providerFactory.create(parameterClass, parameterAnnotations, placeholderHandler);
                }

                if (provider == null) {
                    throw new IllegalArgumentException("Invalid parameter type '%s' defined at method '%s' with parameter name '%s'"
                        .formatted(parameterClass.getSimpleName(), method.getName(), name));
                }

                parameters.put(name, new PlaceholderParameter<>(name, parameterClass, provider));
            }

            PlaceholderMethod<C> placeholderMethod = new PlaceholderMethod<>(caller, parameters);

            if (annotations.contains(Placeholder.class)) {
                Placeholder methodAnnotation = annotations.get(Placeholder.class);
                for (String path : methodAnnotation.value()) {
                    List<PlaceholderNode<C>> nodes = PlaceholderNode.create(path, parameters);
                    placeholders.add(new PlaceholderImpl<>(nodes, placeholderMethod));
                }
            }

            if (annotations.contains(SubPlaceholder.class)) {
                SubPlaceholder methodAnnotation = annotations.get(SubPlaceholder.class);
                for (String path : methodAnnotation.value()) {
                    List<PlaceholderNode<C>> additionalNodes = PlaceholderNode.create(path, parameters);

                    for (List<PlaceholderNode<C>> baseNode : baseNodes) {
                        List<PlaceholderNode<C>> nodes = new ArrayList<>(baseNode);
                        nodes.addAll(additionalNodes);

                        placeholders.add(new PlaceholderImpl<>(nodes, placeholderMethod));
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
