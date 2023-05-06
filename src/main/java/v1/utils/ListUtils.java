package v1.utils;

import v1.model.Generative4jException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListUtils {
    public static <T> Map<T, T> keyValuesToMap(T... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new Generative4jException("parameters should be event");
        }
        final Map<T, T> evenMap = new HashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            evenMap.put(keyValues[i], keyValues[i + 1]);
        }
        return evenMap;
    }
}
