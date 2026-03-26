package org.lushplugins.placeholderhandler.placeholder;

import org.lushplugins.placeholderhandler.stream.MutableStringStream;

@FunctionalInterface
public interface PlaceholderParser<C extends PlaceholderContext> {
    String parse(MutableStringStream input, PlaceholderImpl<C> placeholder, C context);
}
