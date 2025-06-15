package org.lushplugins.placeholderhandler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubPlaceholder {
    /**
     * The input path and aliases. Values can contain spaces,
     * in which case it would automatically walk through the categories and
     * correctly calculate the command path.
     *
     * @return The input path and aliases
     */
    String[] value();
}
