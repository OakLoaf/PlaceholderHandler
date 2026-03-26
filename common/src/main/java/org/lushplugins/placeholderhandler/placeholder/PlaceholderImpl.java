package org.lushplugins.placeholderhandler.placeholder;

import org.lushplugins.placeholderhandler.placeholder.node.LiteralNode;
import org.lushplugins.placeholderhandler.placeholder.node.PlaceholderNode;
import org.lushplugins.placeholderhandler.stream.MutableStringStream;

import java.util.List;

public class PlaceholderImpl<C extends PlaceholderContext> {
    private final List<PlaceholderNode<C>> nodes;
    private final PlaceholderParser<C> parser;

    public PlaceholderImpl(List<PlaceholderNode<C>> nodes, PlaceholderParser<C> parser) {
        this.nodes = nodes;
        this.parser = parser;
    }

    public LiteralNode<C> firstNode() {
        return (LiteralNode<C>) nodes.getFirst();
    }

    public List<PlaceholderNode<C>> nodes() {
        return nodes;
    }

    public String parse(MutableStringStream input, C context) {
        return this.parser.parse(input, this, context);
    }

    public boolean isValid(MutableStringStream input, C context) {
        for (PlaceholderNode<C> node : this.nodes) {
            String parameter = input.readString();
            if (!node.test(parameter, context)) {
                return false;
            }
        }

        return true;
    }
}
