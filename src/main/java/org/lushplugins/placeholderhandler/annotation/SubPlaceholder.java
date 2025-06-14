package org.lushplugins.placeholderhandler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubPlaceholder {
    /**
     * The rawPlaceholder path and aliases. Values can contain spaces,
     * in which case it would automatically walk through the categories and
     * correctly calculate the command path.
     *
     * @return The rawPlaceholder path and aliases
     */
    String[] value();
}
