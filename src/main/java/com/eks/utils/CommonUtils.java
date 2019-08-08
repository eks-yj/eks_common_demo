package com.eks.utils;

import java.util.Map;
import java.util.Set;

public class CommonUtils {
    public static <K,V> V getFirstNotNullValue(Map<K,V> map){
        V value = null;
        if (map != null && map.size() > 0) {
            Set<Map.Entry<K, V>> entrySet = map.entrySet();
            for(Map.Entry<K, V> entry : entrySet){
                value = entry.getValue();
                if (value != null){
                    break;
                }
            }
        }
        return value;
    }
}
