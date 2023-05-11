package me.degeron.questplugin.menu;

import me.degeron.questplugin.Main;
import me.degeron.questplugin.db.PlayerStats;
import me.degeron.questplugin.menu.creator.ButtonsCreator;
import me.degeron.questplugin.menu.creator.QuestItemCreator;
import me.degeron.questplugin.menu.time.QuestsTimer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {

    public static Menu instance = new Menu();

    public Map<Player, Inventory> viewers = new HashMap<>();

    private final ConfigurationSection menuSection = Main.getInstance().getConfig().getConfigurationSection("menu");


    public void show(Player player) {
        player.closeInventory();
        Inventory inventory = Bukkit.createInventory(
                null,
                menuSection.getInt("size"),
                menuSection.getString("name")
        );

        try {

            // Генерация предмета квеста в зависимости что у игрока в бд
            // Если квест не выбран - генерировать предложенный квест, и так далее

            PlayerStats stats = Main.getInstance().getDatabase().getPlayerStatsFromDatabase(player);

            ButtonsCreator.statsItemCreate(inventory, stats);
            ButtonsCreator.cancelButtonCreate(inventory);
            ButtonsCreator.decorCreate(inventory);

            if (stats.getSelectedQuest().equals("")) {
                QuestItemCreator.suggestedQuestItemCreate(
                        inventory,
                        QuestsTimer.instance.getSuggestedQuest());
            }

            else if (stats.getSelectedQuest().equals("done")) {
                QuestItemCreator.doneQuestItemCreate(
                        inventory,
                        QuestsTimer.instance.getSuggestedQuest()
                );
            }

            else if (stats.getSelectedQuest().equals("cancel")) {
                QuestItemCreator.cancelQuestItemCreate(
                        inventory,
                        QuestsTimer.instance.getSuggestedQuest()
                );
            }

            else {
                QuestItemCreator.selectedQuestItemCreate(
                        inventory,
                        stats.getSelectedQuest(),
                        stats.getProgressInQuest()
                );
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        player.openInventory(inventory);
        viewers.put(player, inventory);
    }
}
