package me.degeron.questplugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.degeron.questplugin.menu.event.MainListener;
import me.degeron.questplugin.menu.event.completing.BlockBreakQuest;
import me.degeron.questplugin.menu.event.completing.MobKillQuest;
import me.degeron.questplugin.menu.event.completing.ResourceFindQuest;
import me.degeron.questplugin.menu.time.QuestsTimer;
import me.degeron.questplugin.NPC.NPCListener;
import me.degeron.questplugin.NPC.QuestNPCCommand;
import me.degeron.questplugin.db.Database;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public final class Main extends JavaPlugin {

    private static Main instance;

    private Database database;
    private Economy economy;
    private ProtocolManager manager;
    private YamlConfiguration quests;
    private File questsFile;

    @Override
    public void onEnable() {
        instance = this;

        // Подключение файла с квестами и подключение конфига
        this.questsFile = new File(instance.getDataFolder().getAbsolutePath(), "quests.yml");

        if (!questsFile.exists()) {
            saveResource("quests.yml", false);
        }

        this.quests = YamlConfiguration.loadConfiguration(this.questsFile);

        saveDefaultConfig();

        // Подключение к бд
        try {
            this.database = new Database();
            database.initializeDatabase();
        } catch (SQLException exception) {
            System.out.println("Невозможное подключение к базе данных и таблице.");
            exception.printStackTrace();
        }

        // Регистрация ивента NMS (клик по NPC) и регистрация комманды с обычными ивентами
        this.manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new NPCListener(this, PacketType.Play.Client.USE_ENTITY));

        getCommand("npc").setExecutor(new QuestNPCCommand());

        getServer().getPluginManager().registerEvents(new MainListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakQuest(), this);
        getServer().getPluginManager().registerEvents(new MobKillQuest(), this);

        // Подключение экономики
        if (!setupEconomy()) {
            System.out.println("Vault не найден! Плагин выключен.");
            getServer().getPluginManager().disablePlugin(this) ;
        }

        // Таймер обновления квестов
        QuestsTimer.instance.suggestANewQuest();
        QuestsTimer.instance.timer();
    }

    public boolean setupEconomy() {
        RegisteredServiceProvider<Economy> registeredServiceProvider = getServer().getServicesManager().getRegistration(Economy.class);

        if (registeredServiceProvider == null) {
            return false;
        }
        economy = registeredServiceProvider.getProvider();
        return true;
    }

    @Override
    public void onDisable() {
        this.database.closeConnection();
    }

    public static Main getInstance() {
        return instance;
    }

    public Database getDatabase() {
        return database;
    }

    public YamlConfiguration getQuests() {
        return quests;
    }

    public Economy getEconomy() {
        return economy;
    }
}
