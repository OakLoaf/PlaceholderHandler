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

    public String parse(PlaceholderContext context) {
        return this.parser.parse(context);
    }

    public boolean isValid(PlaceholderContext context) {
        MutableStringStream input = context.input().toMutableCopy();

        for (PlaceholderNode node : nodes) {
            String parameter = input.readString();
            if (!node.test(parameter, context)) {
                return false;
            }
        }

        return true;
    }
}
