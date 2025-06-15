package org.lushplugins.placeholderhandler.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.placeholderhandler.PlaceholderHandler;

public class PlaceholderAPIHook {

    public static void register(PlaceholderHandler instance) {
        instance.placeholders().stream()
            .map(p -> p.firstNode().name())
            .distinct()
            .forEach((identifier) -> new Expansion(instance, identifier)
                .register());
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
            return String.join(", ", this.instance.plugin().getDescription().getAuthors());
        }

        public @NotNull String getVersion() {
            return this.instance.plugin().getDescription().getVersion();
        }
    }
}
