package org.lushplugins.placeholderhandler.annotation;

import org.jetbrains.annotations.NotNull;
import org.lushplugins.placeholderhandler.parameter.ParameterProvider;
import org.lushplugins.placeholderhandler.parameter.PlaceholderMethod;
import org.lushplugins.placeholderhandler.parameter.PlaceholderParameter;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderImpl;
import org.lushplugins.placeholderhandler.util.reflect.MethodCaller;
import org.lushplugins.placeholderhandler.util.reflect.MethodCallerFactory;
import org.lushplugins.placeholderhandler.util.reflect.Reflection;
import org.lushplugins.placeholderhandler.PlaceholderHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class AnnotationHandler {

    public static List<PlaceholderImpl> register(@NotNull Class<?> instanceClass, Object instance, PlaceholderHandler placeholderHandler) {
        List<PlaceholderImpl> placeholders = new ArrayList<>();

        Placeholder placeholderAnnotation = instanceClass.getAnnotation(Placeholder.class);
        String[] basePlaceholders = placeholderAnnotation.value();

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

                ParameterProvider<?> provider;
                ParameterProvider.Factory providerFactory = placeholderHandler.getParameterProviderFactory(instanceClass);
                if (providerFactory != null) {
                    AnnotationList parameterAnnotations = new AnnotationList(parameter);
                    provider = providerFactory.create(instanceClass, parameterAnnotations, placeholderHandler);
                } else {
                    provider = placeholderHandler.getParameterProvider(instanceClass);
                }

                if (provider == null) {
                    throw new IllegalArgumentException("Invalid parameter type defined at method '%s' with parameter name '%s'"
                        .formatted(method.getName(), name));
                }

                parameters.put(name, new PlaceholderParameter<>(name, parameter.getType(), provider));
            }

            PlaceholderMethod placeholderMethod = new PlaceholderMethod(caller, parameters);
            // TODO: construct placeholder
        }

        return placeholders;
    }

    private static boolean containsPlaceholderAnnotation(AnnotationList annotations) {
        return annotations.contains(Placeholder.class)
            || annotations.contains(SubPlaceholder.class);
    }
}
