package at.ac.fhcampuswien.fhmdb.patterns;

import javafx.util.Callback;
import java.util.HashMap;
import java.util.Map;

public class MyFactory implements Callback<Class<?>, Object> {

    private final Map<Class<?>, Object> singletons = new HashMap<>();

    @Override
    public Object call(Class<?> controllerClass) {
        if (singletons.containsKey(controllerClass)) {
            return singletons.get(controllerClass);
        }

        try {
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            singletons.put(controllerClass, controllerInstance);
            return controllerInstance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}