package com.eks.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeepCloneUtils {
    @SuppressWarnings("unchecked")
    public static <T> T deepClone(T t) {
        if (t == null) {
            return null;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(t);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (T)objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fail to deep clone.");
        }
    }
}
