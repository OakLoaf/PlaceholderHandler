package org.lushplugins.placeholderhandler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubPlaceholder {
    /**
     * The input path and aliases. Values can contain underscores,
     * but not spaces.
     *
     * @return The input path and aliases
     */
    String[] value();
}
