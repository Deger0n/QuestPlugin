package me.degeron.questplugin.util;

import me.degeron.questplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtil {
    public static String prefix = "&a[QuestPlugin] ";

    public static void sendMessage(Player player, String msg) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + msg));
    }

    public static void sendTitle(Player player, String msg, String subMsg, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(
                ChatColor.translateAlternateColorCodes('&', msg),
                ChatColor.translateAlternateColorCodes('&', subMsg),
                fadeIn,
                stay,
                fadeOut
        );
    }

    public static void sendTitle(Player player, String msg, String subMsg) {
        player.sendTitle(
                ChatColor.translateAlternateColorCodes('&', msg),
                ChatColor.translateAlternateColorCodes('&', subMsg),
                10,
                70,
                20
        );
    }

    public static void broadcastMessage(String message) {
        for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
            sendMessage(player, message);
        }
    }

    public static void broadcastMessage(String message, String subMessage) {
        for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
            sendTitle(player, message, subMessage);
        }
    }
}
