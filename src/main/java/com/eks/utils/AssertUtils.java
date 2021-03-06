package com.eks.utils;

import java.util.Collection;

@SuppressWarnings("unchecked")
public class AssertUtils {
    public static <T> T notNull(T object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
        return object;
    }
    public static <T extends Collection> T notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.size() == 0) {
            throw new IllegalArgumentException(message);
        }
        return (T)collection;//这里实际上是将collection强转为接收者的类型,反编译一看便知,如: List<String> stringList2 = (List)AssertUtils.notEmpty(stringList, "notEmpty");
    }
    public static <T> T[] notEmpty(T[] objectArray,String message) {
        if (objectArray == null || objectArray.length == 0) {
            throw new IllegalArgumentException(message);
        }
        return objectArray;
    }
    public static String hasText(String text, String message) {
        if (text == null || text.trim().length() == 0) {
            throw new IllegalArgumentException(message);
        }
        return text;
    }
}