package org.lushplugins.placeholderhandler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Placeholder {
    /**
     * The input path and aliases. Values can contain underscores,
     * but not spaces.
     * <p>
     * Parameter placeholders in the format {@code <parameter>} are
     * only supported on methods.
     *
     * @return The input identifier and aliases
     */
    String[] value();
}
