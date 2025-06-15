package org.lushplugins.placeholderhandler.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.stream.MutableStringStream;

public record PlaceholderContext(MutableStringStream input, @Nullable Player player, PlaceholderHandler instance) {}
