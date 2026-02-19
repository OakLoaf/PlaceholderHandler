package org.lushplugins.placeholderhandler.placeholder;

import org.lushplugins.placeholderhandler.placeholder.node.LiteralNode;
import org.lushplugins.placeholderhandler.placeholder.node.PlaceholderNode;
import org.lushplugins.placeholderhandler.stream.MutableStringStream;

import java.util.List;

public class PlaceholderImpl {
    private final List<PlaceholderNode> nodes;
    private final PlaceholderParser parser;

    public PlaceholderImpl(List<PlaceholderNode> nodes, PlaceholderParser parser) {
        this.nodes = nodes;
        this.parser = parser;
    }

    public LiteralNode firstNode() {
        return (LiteralNode) nodes.getFirst();
    }

    public List<PlaceholderNode> nodes() {
        return nodes;
    }

    public String parse(MutableStringStream input, PlaceholderContext context) {
        return this.parser.parse(input, this, context);
    }

    public boolean isValid(MutableStringStream input, PlaceholderContext context) {
        for (PlaceholderNode node : this.nodes) {
            String parameter = input.readString();
            if (!node.test(parameter, context)) {
                return false;
            }
        }

        return true;
    }
}
