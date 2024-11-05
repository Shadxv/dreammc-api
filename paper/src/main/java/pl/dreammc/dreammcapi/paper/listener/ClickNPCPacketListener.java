package pl.dreammc.dreammcapi.paper.listener;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.manager.NPCManager;
import pl.dreammc.dreammcapi.paper.npc.NPC;
import pl.dreammc.dreammcapi.paper.npc.event.NPCClickEvent;
import pl.dreammc.dreammcapi.paper.type.HandType;
import pl.dreammc.dreammcapi.paper.type.InteractionType;

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
        Bukkit.getScheduler().runTask(PaperDreamMCAPI.getInstance(), () -> {
            Bukkit.getPluginManager().callEvent(npcClickEvent);
        });
    }

}
