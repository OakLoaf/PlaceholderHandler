package org.lushplugins.placeholderhandler.placeholder;

import java.util.List;

public class PlaceholderImpl {
    private final List<PlaceholderNode> nodes;
    private final PlaceholderParser parser;

    public PlaceholderImpl(List<PlaceholderNode> nodes, PlaceholderParser parser) {
        this.nodes = nodes;
        this.parser = parser;
    }

    public PlaceholderNode firstNode() {
        return nodes.getFirst();
    }

    public List<PlaceholderNode> nodes() {
        return nodes;
    }

    public String parse(PlaceholderContext context) {
        return this.parser.parse(context);
    }
}
