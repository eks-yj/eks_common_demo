package com.eks.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pinyin4jUtils {
    //字符串是否匹配正则表达式
    public static boolean match(String string, String regexString) {
        Pattern pattern = Pattern.compile(regexString);
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }
    //将单个汉字转成拼音
    private static String convertSingleHanziToPinyin(char hanziChar) throws BadHanyuPinyinOutputFormatCombination {
        HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String[] pinyinStringArray  = PinyinHelper.toHanyuPinyinStringArray(hanziChar, hanyuPinyinOutputFormat);
        //对于多音字，只用第一个拼音
        return pinyinStringArray[0];
    }
    //将汉字转为拼音,fullBoolean标识是否只取首字母,separatorString设置分隔符
    public static String convertHanzi2Pinyin(String hanziString,Boolean fullBoolean,String separatorString) throws BadHanyuPinyinOutputFormatCombination {
        if (hanziString == null){
            return null;
        }
        hanziString = hanziString.trim();
        if ("".equals(hanziString)){
            return hanziString;
        }
        //^[\u2E80-\u9FFF]+$ 匹配所有东亚区的语言
        //^[\u4E00-\u9FFF]+$ 匹配简体和繁体
        //^[\u4E00-\u9FA5]+$ 匹配简体
        String regexString = "^[\u4E00-\u9FFF]+$";
        StringBuilder stringBuilder = new StringBuilder();
        String pinyinString = "";
        for (int i = 0,length = hanziString.length();i < length;i++) {
            char unitChar = hanziString.charAt(i);
            //是汉字，则转拼音
            if (match(String.valueOf(unitChar), regexString)){
                pinyinString = convertSingleHanziToPinyin(unitChar);
                if (fullBoolean) {
                    stringBuilder.append(pinyinString);
                } else {
                    stringBuilder.append(pinyinString.charAt(0));
                }
                if (separatorString != null && i != length - 1){
                    stringBuilder.append(separatorString);
                }
            } else {
                stringBuilder.append(unitChar);
            }
        }
        return stringBuilder.toString();
    }
    public static String getFieldNameFromPinyin(String pinyinString){
        String[] splitStringArray = pinyinString.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0,length = splitStringArray.length;i < length;i++){
            String itemString = splitStringArray[i];
            if (i == 0){
                stringBuilder.append(itemString);
                continue;
            }
            char c = itemString.charAt(0);
            c = Character.toUpperCase(c);
            stringBuilder.append(c);
            stringBuilder.append(itemString.substring(1, itemString.length()));
        }
        String fieldNameString = stringBuilder.toString();
        fieldNameString = fieldNameString.replaceAll(":","");
        return fieldNameString;
    }
    public static String getFieldNameFromHanzi(String hanziString,Boolean fullBoolean,String separatorString) throws BadHanyuPinyinOutputFormatCombination {
        String[] hanziStringArray = hanziString.split("\\(");
        if (hanziStringArray.length > 1){
            hanziString = hanziStringArray[0];
        }
        String pinyinString = Pinyin4jUtils.convertHanzi2Pinyin(hanziString, fullBoolean, separatorString);
        return getFieldNameFromPinyin(pinyinString);
    }
    public static String getMethodNameFromHanzi(String hanziString,Boolean fullBoolean) throws BadHanyuPinyinOutputFormatCombination {
        String fieldNameString = getFieldNameFromHanzi(hanziString, fullBoolean, "_");
        return "set" + Character.toUpperCase(fieldNameString.charAt(0)) + fieldNameString.substring(1, fieldNameString.length());
    }
}