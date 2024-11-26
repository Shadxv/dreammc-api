package pl.dreammc.dreammcapi.paper.manager;

import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.api.util.ClassUtil;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.command.PaperCommand;

import java.util.Set;

public class CommandManager {

    private static final String PLUGIN_COMMAND_PREFIX = "dreammc";

    private void registerCommand(PaperCommand command) {
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

    public static CommandManager getInstance() {
        return PaperDreamMCAPI.getInstance().getCommandManager();
    }
}
