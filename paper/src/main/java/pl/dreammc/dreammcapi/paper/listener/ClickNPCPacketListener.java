package pl.dreammc.dreammcapi.paper.listener;

import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.manager.NPCManager;
import pl.dreammc.dreammcapi.paper.npc.NPC;
import pl.dreammc.dreammcapi.paper.npc.event.NPCClickEvent;
import pl.dreammc.dreammcapi.paper.packet.PacketEvent;
import pl.dreammc.dreammcapi.paper.packet.PacketListener;
import pl.dreammc.dreammcapi.paper.type.HandType;
import pl.dreammc.dreammcapi.paper.type.InteractionType;

public class ClickNPCPacketListener implements PacketListener<ServerboundInteractPacket> {

    @Override
    public void handlePacket(PacketEvent<ServerboundInteractPacket> event) {
        int id = event.getPacket().getEntityId();
        NPC<?> npc = NPCManager.getInstance().getNPC(id);
        if(npc == null) return;

        event.setCancelled(true);

        final InteractionType[] type = new InteractionType[1];
        final HandType[] hand = new HandType[1];
        ServerboundInteractPacket.Handler handler = new ServerboundInteractPacket.Handler() {
            @Override
            public void onInteraction(InteractionHand interactionHand) {
                type[0] = InteractionType.INTERACT;
                hand[0] = HandType.valueOf(interactionHand.name());
            }

            @Override
            public void onInteraction(InteractionHand interactionHand, Vec3 pos) {
                type[0] = InteractionType.INTERACT_AT;
                hand[0] = HandType.valueOf(interactionHand.name());
            }

            @Override
            public void onAttack() {
                type[0] = InteractionType.ATTACK;
                hand[0] = HandType.MAIN_HAND;
            }
        };
        event.getPacket().dispatch(handler);

        NPCClickEvent npcClickEvent = new NPCClickEvent(event.getPlayer(), npc, type[0], hand[0]);
        Bukkit.getScheduler().runTask(PaperDreamMCAPI.getInstance(), bukkitTask -> {
            Bukkit.getPluginManager().callEvent(npcClickEvent);
        });
    }
}
