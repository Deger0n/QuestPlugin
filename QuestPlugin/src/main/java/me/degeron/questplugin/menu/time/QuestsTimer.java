package me.degeron.questplugin.menu.time;

import me.degeron.questplugin.Main;
import me.degeron.questplugin.db.PlayerStats;
import me.degeron.questplugin.menu.Menu;
import me.degeron.questplugin.menu.QuestItem;
import me.degeron.questplugin.menu.creator.QuestItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.time.*;
import java.util.*;

public class QuestsTimer {

    public static QuestsTimer instance = new QuestsTimer();

    private final YamlConfiguration questsFile = Main.getInstance().getQuests();
    private final ConfigurationSection difficultySection = Main.getInstance().getConfig().getConfigurationSection("menu.quest_item.difficulty");

    private final int minuteUpdate = Main.getInstance().getConfig().getInt("menu.minute_update");
    private QuestItem suggestedQuest;

    public void timer() {
        Timer timer = new Timer("QuestUpdate");

        // Поток, перезапускается каждую минуту
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalTime now = LocalTime.now();

                // Если сейчас время обновить квест - делаем это
                // И вместе с этим стираем из бд данные о взятом квесте у игроков, у которых
                // Квест отменён или выполнен
                if (now.getMinute() == minuteUpdate) {
                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                        suggestANewQuest();
                    });
                    try {
                        Main.getInstance().getDatabase().replaceDoneOrCancelSelectedQuests();
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }

                // А если нет - меняем только время
                else {
                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                        suggestedQuest.setMinutes(getMinutesBetween());
                    });
                }

                // Против таймаутов бд
                try {
                    Main.getInstance().getDatabase().againstTimeouts();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

                updateQuestItem();
            }
        }, 0, 60*1000);
    }


    // Выбор рандомного квеста и генерация ссылки на него,
    // Создание по ссылке объекта предложенного квеста
    public void suggestANewQuest() {
        Random random = new Random();
        List<String> questLinks = new ArrayList<>();

        for (String typeKey : questsFile.getKeys(false)) {
            ConfigurationSection typeSection = questsFile.getConfigurationSection(typeKey);
            for (String questKey : typeSection.getKeys(false)) {
                questLinks.add(typeKey + "." + questKey);
            }
        }

        String linkOfQuest = questLinks.get(random.nextInt(0, questLinks.size()));
        if (suggestedQuest != null) {
            while (suggestedQuest.getLink().equals(linkOfQuest)) {
                linkOfQuest = questLinks.get(random.nextInt(0, questLinks.size()));
            }
        }

        ConfigurationSection questSection = questsFile.getConfigurationSection(linkOfQuest);

        suggestedQuest = new QuestItem(
                questSection.getString("name"),
                Material.valueOf(questSection.getString("material")),
                difficultySection.getString(Integer.toString(questSection.getInt("difficulty"))),
                questSection.getString("reward.name"),
                questSection.getString("description"),
                getMinutesBetween(),
                0,
                0,
                linkOfQuest
        );
    }


    // Обновление предмета
    private void updateQuestItem() {
        for (Player player : Menu.instance.viewers.keySet()) {
            try {
                PlayerStats stats = Main.getInstance().getDatabase().getPlayerStatsFromDatabase(player);
                if (stats.getSelectedQuest().equals("")) {
                    QuestItemCreator.suggestedQuestItemCreate(Menu.instance.viewers.get(player), suggestedQuest);
                }
                else if (stats.getSelectedQuest().equals("done")) {
                    QuestItemCreator.doneQuestItemCreate(Menu.instance.viewers.get(player), suggestedQuest);
                }
                else if (stats.getSelectedQuest().equals("cancel")) {
                    QuestItemCreator.cancelQuestItemCreate(Menu.instance.viewers.get(player), suggestedQuest);
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    // Получение времени до обновления квеста
    private int getMinutesBetween() {
        LocalTime now = LocalTime.now();

        if (now.getMinute() == minuteUpdate)
            return 60;

        else if (now.getMinute() > minuteUpdate)
            return 60 - now.getMinute() + minuteUpdate;

        else
            return minuteUpdate - now.getMinute();
    }

    public QuestItem getSuggestedQuest() {
        return suggestedQuest;
    }
}
