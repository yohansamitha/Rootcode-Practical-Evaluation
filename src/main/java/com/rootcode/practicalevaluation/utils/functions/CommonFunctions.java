package com.rootcode.practicalevaluation.utils.functions;

import org.springframework.lang.Nullable;

public class CommonFunctions {
    public static boolean stringIsNullOrEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }
}
