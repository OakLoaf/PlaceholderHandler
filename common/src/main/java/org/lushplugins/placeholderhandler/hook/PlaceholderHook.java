package org.lushplugins.placeholderhandler.hook;

import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderImpl;

import java.util.Collection;

public interface PlaceholderHook {

    void register(PlaceholderHandler instance, Collection<PlaceholderImpl> placeholders);
}
