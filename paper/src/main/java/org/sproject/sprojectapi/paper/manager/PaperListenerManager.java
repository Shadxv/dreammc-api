package org.sproject.sprojectapi.paper.manager;

import com.google.common.reflect.ClassPath;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.sproject.sprojectapi.api.logger.Logger;
import org.sproject.sprojectapi.paper.PaperSProjectAPI;

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

    public void registerListeners() {
        Set<Class<?>> listenerClasses = findAllListenersInPackage("org.sproject.sprojectapi.paper.listeners", Listener.class);

        for (Class<?> listenerClass : listenerClasses) {
            try {
                Listener listener = (Listener) listenerClass.getDeclaredConstructor().newInstance();
                this.registerListener(listener);
            } catch (Exception exception) {
                Logger.sendError(exception.getLocalizedMessage());
            }
        }
    }

    public Set<Class<?>> findAllListenersInPackage(String packageName, Class<?> findClass) {
        try {
            return ClassPath.from(this.plugin.getClass().getClassLoader())
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
        return PaperSProjectAPI.getInstance().getListenerManager();
    }

}
