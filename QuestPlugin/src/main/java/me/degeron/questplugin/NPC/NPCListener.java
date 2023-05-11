package me.degeron.questplugin.NPC;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.degeron.questplugin.Main;
import me.degeron.questplugin.menu.Menu;
import me.degeron.questplugin.menu.event.completing.ResourceFindQuest;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class NPCListener extends PacketAdapter {

    public NPCListener(JavaPlugin plugin, PacketType packetType) {
        super(plugin, packetType);
    }

    // Регистрируем пакет клика по энтити и проверяем НПС ли это.
    // А ещё здесь регистрируется квест поиска ресурсов,
    // Которые игрок должен приносить НПС

    @Override
    public void onPacketReceiving(PacketEvent event) {

        PacketContainer packet = event.getPacket();

        int entityId = packet.getIntegers().read(0);
        if (entityId == NPCManager.instance.getSpecialNpcID()) {

            EnumWrappers.EntityUseAction action = packet.getEnumEntityUseActions().read(0).getAction();
            EnumWrappers.Hand hand = null;

            if (action == EnumWrappers.EntityUseAction.INTERACT) {
                hand = packet.getEnumEntityUseActions().read(0).getHand();
            }

            if (hand == EnumWrappers.Hand.MAIN_HAND || action == EnumWrappers.EntityUseAction.ATTACK) {
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    if (!ResourceFindQuest.isFinder(event.getPlayer()))
                        Menu.instance.show(event.getPlayer());
                });
            }
        }
    }
}