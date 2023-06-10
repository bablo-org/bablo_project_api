package com.github.bablo_org.bablo_project.api.utils;

import static java.util.Collections.emptySet;

import java.util.List;
import java.util.Set;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FirestoreUtils {

    public static Set<String> listToSet(Object list) {
        if (list == null) {
            return emptySet();
        }

        if (!(list instanceof List)) {
            throw new RuntimeException("object is not a list: " + list);
        }

        return Set.copyOf((List<String>) list);
    }
}
