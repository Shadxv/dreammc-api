package org.sproject.sprojectapi.paper.listener;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.apache.maven.model.Build;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.sproject.sprojectapi.paper.PaperSProjectAPI;
import org.sproject.sprojectapi.paper.manager.NPCManager;
import org.sproject.sprojectapi.paper.npc.NPC;
import org.sproject.sprojectapi.paper.npc.event.NPCClickEvent;
import org.sproject.sprojectapi.paper.type.HandType;
import org.sproject.sprojectapi.paper.type.InteractionType;

public class ClickNPCPacketListener implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;

        WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);

        int id = packet.getEntityId();
        NPC<?> npc = NPCManager.getInstance().getNPC(id);
        if(npc == null) return;

        event.setCancelled(true);

        InteractionType type = InteractionType.valueOf(packet.getAction().name());
        HandType hand = HandType.valueOf(packet.getHand().name());
        Player player = (Player) event.getPlayer();

        NPCClickEvent npcClickEvent = new NPCClickEvent(player, npc, type, hand);
        Bukkit.getScheduler().runTask(PaperSProjectAPI.getInstance(), () -> {
            Bukkit.getPluginManager().callEvent(npcClickEvent);
        });
    }

}
