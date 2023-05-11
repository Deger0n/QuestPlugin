package me.degeron.questplugin.NPC;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.degeron.questplugin.Main;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.UUID;

public class NPCManager {

    public static NPCManager instance = new NPCManager();

    private ConfigurationSection npcSection = Main.getInstance().getConfig().getConfigurationSection("npc");

    private ServerPlayer specialNpc;

    // Лютый NMS код для NPC
    public void spawnSpecialNPC(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle(); // Получение игрока NMS

        MinecraftServer server = serverPlayer.getServer(); // Получение сервера
        ServerLevel world = ((CraftWorld) Bukkit.getWorld(npcSection.getString("location.world"))).getHandle(); // Получение мира NPC
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), npcSection.getString("name")); // Создание профиля для NPC

        gameProfile.getProperties().put("textures", new Property("textures",
                npcSection.getString("skin.texture"),
                npcSection.getString("skin.signature")
        )); //Подгрузка скина из конфига

        ServerPlayer npc = new ServerPlayer(server, world, gameProfile, null); // Создание NPC

        // Настройка слоёв скина у NPC и его местоположения
        Object data = null;
        try {
            Field field = net.minecraft.world.entity.player.Player.class.getField("bO");
            field.setAccessible(true);
            data = field.get(npc.getEntityData());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        npc.setPos(npcSection.getInt("location.x")*1.0, npcSection.getInt("location.y")*1.0, npcSection.getInt("location.z")*1.0);
        npc.getEntityData().set((EntityDataAccessor<Byte>) data, ModelPart.getAllBitMasks());

        // Спавн NPC и настройка слоёв его скина (последний пакет)
        ServerGamePacketListenerImpl packetServer = craftPlayer.getHandle().connection;

        packetServer.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));
        packetServer.send(new ClientboundAddPlayerPacket(npc));
        packetServer.send(new ClientboundSetEntityDataPacket(
                npc.getId(),
                npc.getEntityData(),
                false
        ));

        this.specialNpc = npc;
    }

    public int getSpecialNpcID() {
        return (this.specialNpc == null) ? 0 : this.specialNpc.getId();
    }

}
