package pl.dreammc.dreammcapi.velocity.manager;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.api.util.ClassUtil;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;
import pl.dreammc.dreammcapi.velocity.command.VelocityCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandManager {

    private final com.velocitypowered.api.command.CommandManager commandManager;

    public CommandManager(com.velocitypowered.api.command.CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    private void registerCommand(VelocityCommand command, Plugin plugin) {
        CommandMeta.Builder builder = this.commandManager.metaBuilder(command.getName());
        for (String alias : command.getAliases()) {
            builder.aliases(alias);
        }
        builder.plugin(plugin);
        this.commandManager.register(builder.build(), command);
    }

    public void registerCommands(Plugin plugin, ClassLoader classLoader, String packageName) {
        Set<Class<?>> commandClasses = ClassUtil.findAllListenersInPackage(classLoader, packageName, VelocityCommand.class);

        for (Class<?> commandClass : commandClasses) {
            if (VelocityCommand.class.isAssignableFrom(commandClass)) {
                try {
                    VelocityCommand command = (VelocityCommand) commandClass.getDeclaredConstructor().newInstance();
                    this.registerCommand(command, plugin);
                } catch (Exception exception) {
                    Logger.sendError(exception.getLocalizedMessage());
                }
            }
        }
    }

    public static List<String> generateListOfPlayers() {
        List<String> result = new ArrayList<>();
        for(Player player : VelocityDreamMCAPI.getInstance().getServer().getAllPlayers()) {
            result.add(player.getUsername());
        }
        return result;
    }


    public static CommandManager getInstance() {
        return VelocityDreamMCAPI.getInstance().getCommandManager();
    }
}
