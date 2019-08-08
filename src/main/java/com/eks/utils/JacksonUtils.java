package com.eks.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class JacksonUtils {
    public static String convertObjectToJsonString(Object object) throws IOException {
        return new ObjectMapper().writeValueAsString(object);
    }
    public static String convertObjectToOrderOrPrettyJsonString(Object object, Boolean orderBoolean, Boolean prettyBoolean) throws IOException {
        return convertJsonStringToOrderOrPrettyJsonString(convertObjectToJsonString(object),orderBoolean,prettyBoolean);
    }
    public static String convertJsonStringToOrderOrPrettyJsonString(String jsonString,Boolean orderBoolean,Boolean prettyBoolean) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, orderBoolean);
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        final Object object = objectMapper.treeToValue(jsonNode, Object.class);
        if (prettyBoolean){
            ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            return objectWriter.writeValueAsString(object);
        }
        return objectMapper.writeValueAsString(object);
    }
}
