package org.lushplugins.placeholderhandler.placeholder.node;

import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;

public class LiteralNode extends PlaceholderNode {
    private final String name;

    public LiteralNode(String name) {
        super(0);
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean test(String parameter, PlaceholderContext context) {
        return this.name.equals(parameter);
    }
}
