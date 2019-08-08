package com.eks.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtils {
    public static void writeObject(String filePathString,Object object){
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePathString);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            AutoCloseableUtils.close(objectOutputStream);
            AutoCloseableUtils.close(fileOutputStream);
        }
    }
    @SuppressWarnings("unchecked")
    public static <T> T readObject(String filePathString){
        ObjectInputStream objectInputStream = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePathString);
            objectInputStream = new ObjectInputStream(fileInputStream);
            return (T)objectInputStream.readObject();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            AutoCloseableUtils.close(fileInputStream);
            AutoCloseableUtils.close(objectInputStream);
        }
        return null;
    }
}
