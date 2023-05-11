package me.degeron.questplugin.menu.event.completing;

import me.degeron.questplugin.Main;
import me.degeron.questplugin.db.PlayerStats;
import me.degeron.questplugin.menu.event.ManagerQuestsListener;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.SQLException;

public class BlockBreakQuest implements Listener {

    //Оброботчик квеста с поломкой блоков

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        try {
            PlayerStats stats = Main.getInstance().getDatabase().getPlayerStatsFromDatabase(event.getPlayer());
            String linkOfQuest = stats.getSelectedQuest();
            String[] linkSplit = linkOfQuest.split("_");

            if (linkSplit[0].equals("block") && linkSplit[1].equals("break")) {
                Player player = event.getPlayer();

                ConfigurationSection questSection = Main.getInstance().getQuests().getConfigurationSection(linkOfQuest);
                String rewardKey = questSection.getConfigurationSection("reward").getKeys(false).iterator().next();
                String conditionKey = questSection.getConfigurationSection("condition").getKeys(false).iterator().next();

                if (event.getBlock().getType() != Material.valueOf(conditionKey)) return;

                ManagerQuestsListener.manageQuest(player, linkSplit, stats, questSection, rewardKey, conditionKey);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
