package pl.dreammc.dreammcapi.paper.command.test;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import pl.dreammc.dreammcapi.api.command.response.CommandResponse;
import pl.dreammc.dreammcapi.api.command.response.CustomCommandResponse;
import pl.dreammc.dreammcapi.api.command.response.ICommandResponse;
import pl.dreammc.dreammcapi.api.model.ProfileModel;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.command.PaperCommand;
import pl.dreammc.dreammcapi.paper.command.PaperSubcommand;
import pl.dreammc.dreammcapi.paper.command.test.npc.NPCDespawnCommand;
import pl.dreammc.dreammcapi.paper.command.test.npc.NPCMoveHereCommand;
import pl.dreammc.dreammcapi.paper.entity.SmartEntity;
import pl.dreammc.dreammcapi.paper.entity.ai.LookAtPlayerComponent;
import pl.dreammc.dreammcapi.paper.entity.ai.MovementComponent;
import pl.dreammc.dreammcapi.paper.listener.PlayerQuitListener;
import pl.dreammc.dreammcapi.paper.manager.PaperProfileManager;
import pl.dreammc.dreammcapi.paper.npc.ClientSideHumanNPC;
import pl.dreammc.dreammcapi.paper.npc.NPC;

import java.awt.print.Paper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPCTestCommand extends PaperCommand implements Listener {

    @Getter private final Map<Player, NPC<?>> npcs;

    public NPCTestCommand() {
        super("npc",
                "",
                "",
                List.of(),
                null,
                true,
                false);
        this.npcs = new HashMap<>();
        this.registerSubcommand(new NPCDespawnCommand(this));
        this.registerSubcommand(new NPCMoveHereCommand(this));
        PaperDreamMCAPI.getInstance().getListenerManager().registerListener(this);
    }

    @Override
    public @NotNull ICommandResponse execute0(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            return CommandResponse.ALLRIGHT;
        }

        if (this.npcs.containsKey(player)) return new CustomCommandResponse(Component.text("Your NPC has already been spawned"));

        NPC<?> npc = new ClientSideHumanNPC(player, player.getLocation())
                .setGameProfile(((CraftPlayer) player).getProfile())
                .addGoalComponent(new MovementComponent())
                .addGoalComponent(new LookAtPlayerComponent(5.0))
                .spawn();

        this.npcs.put(player, npc);

        return CommandResponse.ALLRIGHT;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.getNpcs().remove(event.getPlayer());
    }
}
