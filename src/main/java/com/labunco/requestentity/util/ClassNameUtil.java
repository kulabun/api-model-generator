package com.labunco.requestentity.util;

/**
 * @author kulabun
 * @since 3/25/17
 */
public class ClassNameUtil {

    private ClassNameUtil() {
    }

    public static String extractClassName(String canonicalName) {
        int simpleNameStartIndex = canonicalName.lastIndexOf(".") + 1;
        return canonicalName.substring(simpleNameStartIndex);
    }

    public static String extractPackageName(String canonicalName) {
        int packageNameEndIndex = canonicalName.lastIndexOf(".");
        return canonicalName.substring(0, packageNameEndIndex);
    }
}
