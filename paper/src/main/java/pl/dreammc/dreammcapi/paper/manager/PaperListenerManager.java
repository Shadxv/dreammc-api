package pl.dreammc.dreammcapi.paper.manager;

import com.google.common.reflect.ClassPath;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PaperListenerManager {

    private final JavaPlugin plugin;

    public PaperListenerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerListener(Listener listener) {
        this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
    }

    public void registerListeners(ClassLoader classLoader, String packageName) {
        Set<Class<?>> listenerClasses = findAllListenersInPackage(classLoader, packageName, Listener.class);

        for (Class<?> listenerClass : listenerClasses) {
            try {
                Listener listener = (Listener) listenerClass.getDeclaredConstructor().newInstance();
                this.registerListener(listener);
            } catch (Exception exception) {
                Logger.sendError(exception.getLocalizedMessage());
            }
        }
    }

    public Set<Class<?>> findAllListenersInPackage(ClassLoader classLoader, String packageName, Class<?> findClass) {
        try {
            return ClassPath.from(classLoader)
                    .getTopLevelClassesRecursive(packageName)
                    .stream()
                    .map(ClassPath.ClassInfo::load)
                    .filter(findClass::isAssignableFrom)
                    .collect(Collectors.toSet());
        } catch (IOException exception) {
            Logger.sendError(exception.getLocalizedMessage());
            return new HashSet<>();
        }
    }

    public static PaperListenerManager getInstance() {
        return PaperDreamMCAPI.getInstance().getListenerManager();
    }

}
