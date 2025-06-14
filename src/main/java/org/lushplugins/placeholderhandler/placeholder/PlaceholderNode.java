package org.lushplugins.placeholderhandler.placeholder;

public class PlaceholderNode {
    private final String name;

    public PlaceholderNode(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public boolean test(String parameter) {
        return this.name.equals(parameter);
    }
}
