package org.lushplugins.placeholderhandler.hook;

import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderContext;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderImpl;

import java.util.Collection;

public interface PlaceholderHook<C extends PlaceholderContext> {

    void register(PlaceholderHandler<C> instance, Collection<PlaceholderImpl<C>> placeholders);
}
