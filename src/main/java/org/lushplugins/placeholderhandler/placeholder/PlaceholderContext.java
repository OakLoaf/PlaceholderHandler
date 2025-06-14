package org.lushplugins.placeholderhandler.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.stream.StringStream;

public record PlaceholderContext(StringStream rawPlaceholder, @Nullable Player player, PlaceholderHandler instance) {}
