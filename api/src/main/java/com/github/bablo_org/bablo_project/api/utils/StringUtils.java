package com.github.bablo_org.bablo_project.api.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
