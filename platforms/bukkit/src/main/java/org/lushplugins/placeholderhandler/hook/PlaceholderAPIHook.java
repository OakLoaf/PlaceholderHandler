package org.lushplugins.placeholderhandler.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.placeholderhandler.PlaceholderHandler;
import org.lushplugins.placeholderhandler.context.BukkitPlaceholderContext;
import org.lushplugins.placeholderhandler.placeholder.PlaceholderImpl;

import java.util.*;

public class PlaceholderAPIHook implements PlaceholderHook {
    private final JavaPlugin plugin;
    private final Map<String, Expansion> expansions = new HashMap<>();

    public PlaceholderAPIHook(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register(PlaceholderHandler instance, Collection<PlaceholderImpl> placeholders) {
        placeholders.stream()
            .map(placeholder -> placeholder.firstNode().name())
            .distinct()
            .filter(identifier -> !this.expansions.containsKey(identifier))
            .forEach((identifier) -> {
                Expansion expansion = new Expansion(instance, this.plugin, identifier);
                expansion.register();
                this.expansions.put(identifier, expansion);
            });
    }

    public static class Expansion extends PlaceholderExpansion {
        private final PlaceholderHandler instance;
        private final JavaPlugin plugin;
        private final String identifier;

        public Expansion(PlaceholderHandler instance, JavaPlugin plugin, String identifier) {
            this.instance = instance;
            this.plugin = plugin;
            this.identifier = identifier;
        }

        public String onPlaceholderRequest(Player player, @NotNull String params) {
            String placeholder = "%" + identifier + "_" + params + "%";
            return this.instance.parsePlaceholder(new BukkitPlaceholderContext(placeholder, player, this.instance));
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
            return String.join(", ", this.plugin.getDescription().getAuthors());
        }

        public @NotNull String getVersion() {
            return this.plugin.getDescription().getVersion();
        }
    }
}
