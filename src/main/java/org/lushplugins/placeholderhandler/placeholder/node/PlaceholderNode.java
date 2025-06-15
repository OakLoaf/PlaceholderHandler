package org.lushplugins.placeholderhandler.placeholder.node;

import org.jetbrains.annotations.NotNull;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;

public abstract class PlaceholderNode implements Comparable<PlaceholderNode> {
    private final int priority;

    public PlaceholderNode(int priority) {
        this.priority = priority;
    }

    public int priority() {
        return priority;
    }

    public abstract boolean test(String parameter, PlaceholderContext context);

    @Override
    public int compareTo(@NotNull PlaceholderNode other) {
        return Integer.compare(this.priority, other.priority());
    }
}
