package org.lushplugins.placeholderhandler.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderImpl;

import java.util.*;

public class PlaceholderAPIHook implements PlaceholderHook {
    private final Map<String, Expansion> expansions = new HashMap<>();

    @Override
    public void register(PlaceholderHandler instance, Collection<PlaceholderImpl> placeholders) {
        placeholders.stream()
            .map(placeholder -> placeholder.firstNode().name())
            .distinct()
            .filter(identifier -> !this.expansions.containsKey(identifier))
            .forEach((identifier) -> {
                Expansion expansion = new Expansion(instance, identifier);
                expansion.register();
                this.expansions.put(identifier, expansion);
            });
    }

    public static class Expansion extends PlaceholderExpansion {
        private final PlaceholderHandler instance;
        private final String identifier;

        public Expansion(PlaceholderHandler instance, String identifier) {
            this.instance = instance;
            this.identifier = identifier;
        }

        public String onPlaceholderRequest(Player player, @NotNull String params) {
            String placeholder = "%" + identifier + "_" + params + "%";
            return this.instance.parsePlaceholder(placeholder, player);
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
