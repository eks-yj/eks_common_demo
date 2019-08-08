package com.eks.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Utils {
    private static final Base64.Decoder DECODER = Base64.getDecoder();
    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    public static String encodeString(String uncodedString) throws UnsupportedEncodingException {
        return encodeString(uncodedString, CharsetName.UTF_8,CharsetName.UTF_8);
    }
    public static String encodeString(String uncodedString, String inputCharsetName, String outputCharsetName) throws UnsupportedEncodingException {
        byte[] byteArray = uncodedString.getBytes(inputCharsetName);
        byteArray = ENCODER.encode(byteArray);
        return new String(byteArray,outputCharsetName);
    }
    public static String decodeString(String codedString) throws UnsupportedEncodingException {
        return decodeString(codedString, CharsetName.UTF_8,CharsetName.UTF_8);
    }
    public static String decodeString(String codedString, String charsetName, String outputCharsetName) throws UnsupportedEncodingException {
        byte[] byteArray = codedString.getBytes(charsetName);
        byteArray = DECODER.decode(byteArray);
        return new String(byteArray,outputCharsetName);
    }
}
