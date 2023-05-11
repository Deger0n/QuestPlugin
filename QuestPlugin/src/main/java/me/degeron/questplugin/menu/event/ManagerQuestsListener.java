package me.degeron.questplugin.menu.event;

import me.degeron.questplugin.Main;
import me.degeron.questplugin.db.PlayerStats;
import me.degeron.questplugin.menu.time.QuestsTimer;
import me.degeron.questplugin.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class ManagerQuestsListener {

    // Зачисляем игроку прогресс в квесте

    private static final ConfigurationSection messages = Main.getInstance().getConfig().getConfigurationSection("messages");

    public static void manageQuest(Player player, String[] linkSplit, PlayerStats stats, ConfigurationSection questSection, String rewardKey, String conditionKey) throws SQLException {

        stats.setProgressInQuest(stats.getProgressInQuest() + 1);

        ChatUtil.sendMessage(player,
                messages.getString("did_the_part")
                        .replace("%quest_name%", questSection.getString("name"))
                        .replace("%progress%", Integer.toString(stats.getProgressInQuest()))
                        .replace("%full%", questSection.getString("condition." + conditionKey))
        );

        if (stats.getProgressInQuest() >= questSection.getInt("condition." + conditionKey)) {
            if (linkSplit[2].equals("coin")) {
                Main.getInstance().getEconomy().depositPlayer(player, questSection.getInt("reward." + rewardKey));
            }

            else {
                int quantity = questSection.getInt("reward." + rewardKey);

                while (quantity > 0) {
                    if (quantity >= 64)
                        player.getInventory().addItem(
                                new ItemStack(Material.valueOf(rewardKey), 64)
                        );
                    else
                        player.getInventory().addItem(
                                new ItemStack(Material.valueOf(rewardKey), quantity)
                        );
                    quantity -= 64;
                }
            }

            ChatUtil.sendMessage(player,
                    messages.getString("did_the_quest")
                            .replace("%quest_name%", questSection.getString("name"))
            );

            if (stats.getSelectedQuest().equals(QuestsTimer.instance.getSuggestedQuest().getLink()))
                stats.setSelectedQuest("done");
            else stats.setSelectedQuest("");

            stats.setProgressInQuest(0);
            stats.setCompleted(stats.getCompleted() + 1);
        }

        Main.getInstance().getDatabase().updatePlayerStats(stats);
    }
}
