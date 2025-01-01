package pl.dreammc.dreammcapi.paper.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.api.util.ClassUtil;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.command.PaperCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandManager {

    private static final String PLUGIN_COMMAND_PREFIX = "dreammc";

    public void registerCommand(PaperCommand command) {
        PaperDreamMCAPI.getInstance().getServer().getCommandMap().register(command.getName(), PLUGIN_COMMAND_PREFIX, command);
    }

    public void registerCommands(ClassLoader classLoader, String packageName) {
        Set<Class<?>> commandClasses = ClassUtil.findAllListenersInPackage(classLoader, packageName, PaperCommand.class);

        for (Class<?> commandClass : commandClasses) {
            if (PaperCommand.class.isAssignableFrom(commandClass)) {
                try {
                    PaperCommand command = (PaperCommand) commandClass.getDeclaredConstructor().newInstance();
                    this.registerCommand(command);
                } catch (Exception exception) {
                    Logger.sendError(exception.getLocalizedMessage());
                }
            }
        }
    }

    public static List<String> generateListOfPlayers() {
        List<String> result = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            result.add(player.getName());
        }
        return result;
    }

    public static CommandManager getInstance() {
        return PaperDreamMCAPI.getInstance().getCommandManager();
    }
}
