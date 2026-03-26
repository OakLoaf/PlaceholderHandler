package org.lushplugins.placeholderhandler.placeholder;

import org.lushplugins.placeholderhandler.stream.MutableStringStream;

@FunctionalInterface
public interface PlaceholderParser {
    String parse(MutableStringStream input, PlaceholderImpl placeholder, PlaceholderContext context);
}
