package org.lushplugins.placeholderhandler.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.placeholderhandler.PlaceholderHandler;

import java.util.Collections;
import java.util.List;

public class PlaceholderAPIHook {

    public PlaceholderAPIHook(PlaceholderHandler instance) {
        // TODO: Register expansion for each identifier
        List<String> identifiers = Collections.emptyList();
        for (String identifier : identifiers) {
            new Expansion(instance, identifier)
                .register();
        }
    }

    public static class Expansion extends PlaceholderExpansion {
        private final PlaceholderHandler instance;
        private final String identifier;

        public Expansion(PlaceholderHandler instance, String identifier) {
            this.instance = instance;
            this.identifier = identifier;
        }

        public String onPlaceholderRequest(Player player, @NotNull String params) {
            return this.instance.parsePlaceholder(params, player);
        }

        public boolean persist() {
            return true;
        }

        public boolean canRegister() {
            return true;
        }

        public @NotNull String getIdentifier() {
            return this.identifier;
        }

        public @NotNull String getAuthor() {
            return String.join(", ", this.instance.getPlugin().getDescription().getAuthors());
        }

        public @NotNull String getVersion() {
            return this.instance.getPlugin().getDescription().getVersion();
        }
    }
}
