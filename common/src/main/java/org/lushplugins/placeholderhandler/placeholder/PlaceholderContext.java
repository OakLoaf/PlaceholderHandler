package org.lushplugins.placeholderhandler.placeholder;

import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.stream.StringStream;

public class PlaceholderContext {
    private final StringStream input;
    private final PlaceholderHandler instance;

    public PlaceholderContext(String input, PlaceholderHandler instance) {
        this.input = StringStream.create(input.substring(1, input.length() - 1));
        this.instance = instance;
    }

    public String source() {
        return input.source();
    }

    public StringStream input() {
        return input;
    }

    public PlaceholderHandler instance() {
        return instance;
    }
}
