package org.lushplugins.placeholderhandler.placeholder;

@FunctionalInterface
public interface PlaceholderParser {
    String parse(PlaceholderContext context);
}
