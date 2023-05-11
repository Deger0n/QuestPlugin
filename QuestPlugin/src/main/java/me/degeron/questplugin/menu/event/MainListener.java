package me.degeron.questplugin.menu.event;

import me.degeron.questplugin.Main;
import me.degeron.questplugin.NPC.NPCManager;
import me.degeron.questplugin.db.PlayerStats;
import me.degeron.questplugin.menu.Menu;
import me.degeron.questplugin.menu.QuestItem;
import me.degeron.questplugin.menu.creator.ButtonsCreator;
import me.degeron.questplugin.menu.creator.QuestItemCreator;
import me.degeron.questplugin.menu.time.QuestsTimer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class MainListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Menu.instance.viewers.remove((Player) event.getPlayer());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (Menu.instance.viewers.containsKey((Player) event.getWhoClicked())) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();

            int slot = event.getSlot();
            int slotCancelItem = Main.getInstance().getConfig().getInt("menu.buttons.cancel_item.slot");
            int slotQuestItem = Main.getInstance().getConfig().getInt("menu.quest_item.slot");

            if (slot == slotCancelItem) player.closeInventory();
            if (slot != slotQuestItem) return;

            try {
                PlayerStats stats = Main.getInstance().getDatabase().getPlayerStatsFromDatabase(player);

                // Если был нажат предложенный квест, то ставим игроку квест,
                // Который он выбрал, нажав на предложенный
                if (stats.getSelectedQuest().equals("")) {
                    stats.setSelectedQuest(QuestsTimer.instance.getSuggestedQuest().getLink());
                    QuestItemCreator.selectedQuestItemCreate(event.getInventory(), stats.getSelectedQuest(), stats.getProgressInQuest());
                }

                // Игнорируем любые другие нажатия, кроме того, при котором
                // У игрока уже взят квест. Отменяем его

                else if (!stats.getSelectedQuest().equals("done")
                        && !stats.getSelectedQuest().equals("cancel")
                        && !stats.getSelectedQuest().equals("")) {

                    if (stats.getSelectedQuest().equals(QuestsTimer.instance.getSuggestedQuest().getLink()))
                        stats.setSelectedQuest("cancel");
                    else stats.setSelectedQuest("");

                    stats.setProgressInQuest(0);
                    stats.setCanceled(stats.getCanceled() + 1);

                    ButtonsCreator.statsItemCreate(event.getInventory(), stats);
                    QuestItemCreator.cancelQuestItemCreate(event.getInventory(), QuestsTimer.instance.getSuggestedQuest());
                }

                Main.getInstance().getDatabase().updatePlayerStats(stats);

            } catch (SQLException exception) {
                exception.printStackTrace();
            }

        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        NPCManager.instance.spawnSpecialNPC(event.getPlayer());
    }
}
