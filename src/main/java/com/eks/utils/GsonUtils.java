package com.eks.utils;

import com.google.gson.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class GsonUtils {
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Method JSON_PRIMITIVE_SET_VALUE_METHOD = null;
    private static KeyComparator KEY_COMPARATOR = new KeyComparator();
    private static class KeyComparator implements Comparator<String> {
        @Override
        public int compare(String firstString, String secondString) {
            return firstString.compareTo(secondString);
        }
    }
    static {
        try {
            JSON_PRIMITIVE_SET_VALUE_METHOD = JsonPrimitive.class.getDeclaredMethod("setValue",Object.class);
            JSON_PRIMITIVE_SET_VALUE_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    public static Gson getGsonInstance(){
        return GSON;
    }
    public static Method getJsonPrimitiveSetValueMethod(){
        return JSON_PRIMITIVE_SET_VALUE_METHOD;
    }
    public static String convertObjectToJsonString(Object object){
        return GSON.toJson(object);
    }
    public static JsonElement convertJsonStringToJsonElement(String jsonString){
        return JSON_PARSER.parse(jsonString);
    }
    public static JsonElement deepCloneJsonElement(JsonElement jsonElement){
        return convertJsonStringToJsonElement(jsonElement.toString());
    }
    public static void trimJsonElement(JsonElement jsonElement) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (jsonElement != null) {
            if (jsonElement.isJsonArray()) {
                trimJsonElement(jsonElement.getAsJsonArray());
            }
            if (jsonElement.isJsonObject()) {
                trimJsonElement(jsonElement.getAsJsonObject());
            }
            if (jsonElement.isJsonPrimitive()) {
                trimJsonElement(jsonElement.getAsJsonPrimitive());
            }
        }
    }
    private static void trimJsonElement(JsonObject jsonObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            trimJsonElement(entry.getValue());
        }
    }
    private static void trimJsonElement(JsonArray jsonArray) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Iterator<JsonElement> jsonElementIterator = jsonArray.iterator();
        while (jsonElementIterator.hasNext()){
            trimJsonElement(jsonElementIterator.next());
        }
    }
    private static void trimJsonElement(JsonPrimitive jsonPrimitive) throws InvocationTargetException, IllegalAccessException {
        if (jsonPrimitive.isString()){
            String string = jsonPrimitive.getAsString();
            if (!"".equals(string)){
                JSON_PRIMITIVE_SET_VALUE_METHOD.invoke(jsonPrimitive,string.trim());
            }
        }
    }
    public static JsonElement getOnlyJsonElementFromJsonArray(JsonArray jsonArray){
        if (jsonArray.size() != 1){
            throw new RuntimeException("The size of JsonArray should be one.");
        }
        return jsonArray.get(0);
    }
    public static String convertJsonElementToPrettyJsonString(JsonElement jsonElement){
        return PRETTY_GSON.toJson(jsonElement);
    }
    public static void sortJsonElement(JsonElement jsonElement){
        if (jsonElement.isJsonObject()) {
            Map<String, JsonElement> treeMap = new TreeMap<>(KEY_COMPARATOR);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
            for(Map.Entry<String, JsonElement> entry : entrySet){
                treeMap.put(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, JsonElement> entry : treeMap.entrySet()) {
                jsonObject.remove(entry.getKey());
                jsonObject.add(entry.getKey(), entry.getValue());
                sortJsonElement(entry.getValue());
            }
        }else if (jsonElement.isJsonArray()){
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for(JsonElement subJsonElement : jsonArray){
                sortJsonElement(subJsonElement);
            }
        }
    }
    @SuppressWarnings("ConstantConditions")
    public static JsonElement getJsonElementByXpath(JsonElement parentJsonElement, String xpathString){
        JsonObject jsonObject = null;
        if (parentJsonElement.isJsonArray()){
            JsonArray jsonArray = parentJsonElement.getAsJsonArray();
            if (jsonArray.size() != 1){
                throw new RuntimeException("The size of parent JsonArray should be one.");
            }
            jsonObject = jsonArray.get(0).getAsJsonObject();
        }else if (parentJsonElement.isJsonObject()){
            jsonObject = parentJsonElement.getAsJsonObject();
        }else{
            throw new RuntimeException("Parent JsonElement should be JsonArray or JsonObject.");
        }
        JsonElement jsonElement = null;
        String[] splitXpathStringArray = xpathString.split("/");
        for(int i = 0,lastInt = splitXpathStringArray.length - 1;i < splitXpathStringArray.length;i++){
            if (i == lastInt){
                jsonElement = jsonObject.get(splitXpathStringArray[i]);
                break;
            }
            jsonObject = jsonObject.get(splitXpathStringArray[i]).getAsJsonObject();
        }
        if (jsonElement == null){
            throw new RuntimeException("Could get JsonElement by provided xpath.");
        }
        return jsonElement;
    }
    public static Boolean compareJsonElement(JsonElement firstJsonElement, JsonElement secondJsonElement) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (firstJsonElement == null && secondJsonElement == null){
            return true;
        }
        String firstString = getStringFromJsonPrimitive(firstJsonElement);
        String secondString = getStringFromJsonPrimitive(secondJsonElement);
        return firstString.equals(secondString);
    }
    private static String getStringFromJsonPrimitive(JsonElement jsonElement) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (jsonElement == null){
            return "";
        }
        GsonUtils.trimJsonElement(jsonElement);
        if(!jsonElement.isJsonPrimitive()){
            throw new RuntimeException("Support 'JsonPrimitive' only.");
        }
        JsonPrimitive firstJsonPrimitive = jsonElement.getAsJsonPrimitive();
        if (!firstJsonPrimitive.isString()){
            throw new RuntimeException("Support 'String' only.");
        }
        String string = firstJsonPrimitive.getAsString();
        if (string == null){
            return "";
        }
        return string;
    }
}