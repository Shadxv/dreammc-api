package pl.dreammc.dreammcapi.paper.manager;

import com.mongodb.lang.Nullable;
import org.bukkit.entity.Player;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.input.InputModel;

import java.util.HashMap;
import java.util.Map;

public class InputManager {

    private final Map<Player, InputModel> activeInputs;

    public InputManager() {
        this.activeInputs = new HashMap<>();
    }

    public boolean isWaitingForInput(Player player) {
        return this.activeInputs.containsKey(player);
    }

    public boolean addInputRequest(InputModel model) {
        if(!this.isWaitingForInput(model.getOwner())) {
            this.activeInputs.put(model.getOwner(), model);
            return true;
        }
        return false;
    }

    public void removeInputRequest(Player player) {
        if(this.isWaitingForInput(player))
            this.activeInputs.remove(player);
    }

    public @Nullable InputModel getInputRequest(Player player) {
        return this.activeInputs.get(player);
    }

    public static InputManager getInstance() {
        return PaperDreamMCAPI.getInstance().getInputManager();
    }
}