package me.degeron.questplugin.menu.event.completing;

import me.degeron.questplugin.Main;
import me.degeron.questplugin.db.PlayerStats;
import me.degeron.questplugin.menu.event.ManagerQuestsListener;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.sql.SQLException;


public class ResourceFindQuest{

    // Обработчик квеста с нахождением предмета, вызывается в классе NPCListener

    public static boolean isFinder(Player player) {
        try {
            PlayerStats stats = Main.getInstance().getDatabase().getPlayerStatsFromDatabase(player);
            String linkOfQuest = stats.getSelectedQuest();
            String[] linkSplit = linkOfQuest.split("_");

            if (linkSplit[0].equals("resource") && linkSplit[1].equals("find")) {

                ConfigurationSection questSection = Main.getInstance().getQuests().getConfigurationSection(linkOfQuest);
                String rewardKey = questSection.getConfigurationSection("reward").getKeys(false).iterator().next();
                String conditionKey = questSection.getConfigurationSection("condition").getKeys(false).iterator().next();

                if (player.getInventory().getItemInMainHand().getType() != Material.valueOf(conditionKey)) return false;

                ManagerQuestsListener.manageQuest(player, linkSplit, stats, questSection, rewardKey, conditionKey);
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}