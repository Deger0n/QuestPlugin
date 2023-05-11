package me.degeron.questplugin.menu.creator;

import me.degeron.questplugin.Main;
import me.degeron.questplugin.db.PlayerStats;
import me.degeron.questplugin.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ButtonsCreator {

    private static final ConfigurationSection menuSection = Main.getInstance().getConfig().getConfigurationSection("menu");

    // Создание декора
    public static void decorCreate(Inventory inventory) {
        for (String decorKey : menuSection.getConfigurationSection("decor").getKeys(false)) {
            ConfigurationSection decorItemSection = menuSection.getConfigurationSection("decor." + decorKey);

            ItemStack decorItemStack = new ItemBuilder(
                    new ItemStack(Material.valueOf(decorKey))
            )
                    .setName(decorItemSection.getString("name"))
                    .build();

            for (int slot : decorItemSection.getIntegerList("slots")) {
                inventory.setItem(slot, decorItemStack);
            }
        }
    }

    // Создание кнопки выхода из меню квестов
    public static void cancelButtonCreate(Inventory inventory) {
        ConfigurationSection buttonSection = menuSection.getConfigurationSection("buttons.cancel_item");

        inventory.setItem(
                buttonSection.getInt("slot"),
                new ItemBuilder(
                        new ItemStack(Material.valueOf(buttonSection.getString("material")))
                )
                        .setName(buttonSection.getString("name"))
                        .setLore(buttonSection.getStringList("lore"))
                        .build()
        );
    }

    // Создание предмета со статистикой
    public static void statsItemCreate(Inventory inventory, PlayerStats stats) {
        ConfigurationSection itemSection = menuSection.getConfigurationSection("buttons.stats_item");

        inventory.setItem(
                itemSection.getInt("slot"),
                new ItemBuilder(
                        new ItemStack(Material.valueOf(itemSection.getString("material")))
                )
                        .setName(itemSection.getString("name"))
                        .setLore(
                                replaceStatsItemLore(
                                        itemSection.getStringList("lore"),
                                        stats
                                )
                        )
                        .build()
        );
    }

    private static List<String> replaceStatsItemLore(List<String> lore, PlayerStats stats) {
        List<String> newLore = new ArrayList<>();
        for (String line : lore) {
            newLore.add(
                    line
                    .replace("%completed%", Integer.toString(stats.getCompleted()))
                    .replace("%canceled%", Integer.toString(stats.getCanceled()))
            );
        }
        return newLore;
    }
}
