package org.lushplugins.placeholderhandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.lushplugins.placeholderhandler.annotation.Placeholder;
import org.lushplugins.placeholderhandler.annotation.SubPlaceholder;

@Placeholder({"test", "example"})
public class Test {

    @SubPlaceholder("generic")
    public String generic() {
        return "generic";
    }

    @SubPlaceholder("name")
    public String name(Player player) {
        return player.getName();
    }

    @Placeholder("example2_uuid")
    public String uuid(Player player) {
        return player.getName();
    }

    @SubPlaceholder("coloured_<content>")
    public String coloured(String content) {
        return ChatColor.translateAlternateColorCodes('&', content);
    }
}
