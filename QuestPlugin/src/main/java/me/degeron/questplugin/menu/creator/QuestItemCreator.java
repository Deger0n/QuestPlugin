package me.degeron.questplugin.menu.creator;

import me.degeron.questplugin.Main;
import me.degeron.questplugin.menu.QuestItem;
import me.degeron.questplugin.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestItemCreator {

    private static final ConfigurationSection itemSection = Main.getInstance().getConfig().getConfigurationSection("menu.quest_item");
    private static final YamlConfiguration questsFile = Main.getInstance().getQuests();

    // Создание предложенного предмета плагином в инвентаре плагина
    public static void suggestedQuestItemCreate(Inventory inventory, QuestItem suggestedQuest) {
        inventory.setItem(
                itemSection.getInt("slot"),
                new ItemBuilder(
                        new ItemStack(Material.valueOf(itemSection.getString("suggested.material")))
                )
                        .setName(itemSection.getString("suggested.name")
                                    .replace("%name%", suggestedQuest.getName()))
                        .setLore(
                                replaceQuestItemLore(
                                        itemSection.getStringList("suggested.lore"),
                                        suggestedQuest
                                )
                        )
                        .build()
            );
    }

    // Создание выбранного квеста там же
    public static void selectedQuestItemCreate(Inventory inventory, String linkOfQuest, int progress) {
        ConfigurationSection questSection = questsFile.getConfigurationSection(linkOfQuest);

        QuestItem questItem = new QuestItem(
                questSection.getString("name"),
                Material.valueOf(questSection.getString("material")),
                itemSection.getString("difficulty." + questSection.getInt("difficulty")),
                questSection.getString("reward.name"),
                questSection.getString("description"),
                -1,
                progress,
                questSection.getInt("condition." + questSection.getConfigurationSection("condition").getKeys(false).iterator().next()),
                linkOfQuest
        );

        inventory.setItem(
                itemSection.getInt("slot"),
                new ItemBuilder(
                        new ItemStack(Material.valueOf(questSection.getString("material")))
                )
                        .setName(itemSection.getString("selected.name")
                                .replace("%name%", questSection.getString("name")))
                        .setLore(
                                replaceQuestItemLore(
                                        itemSection.getStringList("selected.lore"),
                                        questItem
                                )
                        )
                        .build()
        );
    }

    // Если квест выполнен, а сервер ещё его не обновил - будет создан этот предмет
    public static void doneQuestItemCreate(Inventory inventory, QuestItem questItem) {
        inventory.setItem(
                itemSection.getInt("slot"),
                new ItemBuilder(
                        new ItemStack(Material.valueOf(itemSection.getString("done.material")))
                )
                        .setName(itemSection.getString("done.name"))
                        .setLore(
                                replaceQuestItemLore(
                                        itemSection.getStringList("done.lore"),
                                        questItem
                                )
                        )
                        .build()
        );
    }

    // Анологично с прошлым, только если игрок отменил квест, а не выполнил,
    // До того, как квест сервера обновился
    public static void cancelQuestItemCreate(Inventory inventory, QuestItem questItem) {
        inventory.setItem(
                itemSection.getInt("slot"),
                new ItemBuilder(
                        new ItemStack(Material.valueOf(itemSection.getString("cancel.material")))
                )
                        .setName(itemSection.getString("cancel.name"))
                        .setLore(
                                replaceQuestItemLore(
                                        itemSection.getStringList("cancel.lore"),
                                        questItem
                                )
                        )
                        .build()
        );
    }

    // Настраиваем лор предмета
    private static List<String> replaceQuestItemLore(List<String> lore, QuestItem quest) {
        List<String> newLore = new ArrayList<>();
        for (String line : lore) {
            newLore.add(
                    line
                            .replace("%name%", quest.getName())
                            .replace("%difficulty%", quest.getDifficulty())
                            .replace("%reward%", quest.getReward())
                            .replace("%description%", quest.getDescription())
                            .replace("%minutes%", Integer.toString(quest.getMinutes()))
                            .replace("%progress%", Integer.toString(quest.getProgress()))
                            .replace("%full%", Integer.toString(quest.getFull()))
            );
        }
        return newLore;
    }
}
