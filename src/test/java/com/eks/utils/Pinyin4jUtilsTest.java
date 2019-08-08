package com.eks.utils;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.junit.Test;

public class Pinyin4jUtilsTest {
    @Test
    public void test1() throws BadHanyuPinyinOutputFormatCombination {
        String pinyinString = Pinyin4jUtils.convertHanzi2Pinyin("商品id", true, "_");
        System.out.println(pinyinString);
    }
}
