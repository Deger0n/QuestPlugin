package me.degeron.questplugin.menu.event.completing;

import me.degeron.questplugin.Main;
import me.degeron.questplugin.db.PlayerStats;
import me.degeron.questplugin.menu.event.ManagerQuestsListener;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.sql.SQLException;

public class MobKillQuest implements Listener {

    // Оброботчик квеста с убийством мобов

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            try {
                PlayerStats stats = Main.getInstance().getDatabase().getPlayerStatsFromDatabase(event.getEntity().getKiller());
                String linkOfQuest = stats.getSelectedQuest();
                String[] linkSplit = linkOfQuest.split("_");

                if (linkSplit[0].equals("mob") && linkSplit[1].equals("kill")) {
                    Player player = event.getEntity().getKiller();

                    ConfigurationSection questSection = Main.getInstance().getQuests().getConfigurationSection(linkOfQuest);
                    String rewardKey = questSection.getConfigurationSection("reward").getKeys(false).iterator().next();
                    String conditionKey = questSection.getConfigurationSection("condition").getKeys(false).iterator().next();

                    if (event.getEntity().getType() != EntityType.valueOf(conditionKey)) return;

                    ManagerQuestsListener.manageQuest(player, linkSplit, stats, questSection, rewardKey, conditionKey);

                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }
}
