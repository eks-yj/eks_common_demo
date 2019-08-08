package com.eks.utils;

public class AutoCloseableUtils {
    public static void close(AutoCloseable autoCloseable){
        try {
            if (autoCloseable != null){
                autoCloseable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
