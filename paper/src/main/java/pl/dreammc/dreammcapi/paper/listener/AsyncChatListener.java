package pl.dreammc.dreammcapi.paper.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.dreammc.dreammcapi.paper.input.InputModel;
import pl.dreammc.dreammcapi.paper.manager.InputManager;

public class AsyncChatListener implements Listener {

    @EventHandler
    public void onChatMessage(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Component message = event.message();

        // Looks for input request
        if(InputManager.getInstance().isWaitingForInput(player)) {
            event.setCancelled(true);
            InputModel inputModel = InputManager.getInstance().getInputRequest(player);
            if(inputModel.getType() == InputModel.InputType.CHAT) {
                inputModel.setInputValue(((TextComponent) message).content());
                if(!inputModel.validate()) inputModel.reuseChat(Component.text("Invalid value. Please try agian!"));
                else InputManager.getInstance().removeInputRequest(player);
                return;
            }
        }
    }

}
