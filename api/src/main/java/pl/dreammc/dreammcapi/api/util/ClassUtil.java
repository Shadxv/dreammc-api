package pl.dreammc.dreammcapi.api.util;

import com.google.common.reflect.ClassPath;
import pl.dreammc.dreammcapi.api.logger.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassUtil {

    public static Set<Class<?>> findAllListenersInPackage(ClassLoader classLoader, String packageName, Class<?> findClass) {
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

}
