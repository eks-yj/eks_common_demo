package com.eks.utils;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

public class ClipboardUtilsTest {
    private static void clickAndSend(){
        RobotUtils.clickMouse(500,500, -1322, 586);
        RobotUtils.pressKey(500, false, KeyEvent.VK_CONTROL,KeyEvent.VK_V);
        RobotUtils.pressKey(500, true,KeyEvent.VK_ENTER);
    }
    @Test
    public void test1() throws IOException {
        String filePathString = FileUtils.generatePathBaseProjectPath("extra/image/logo.jpg");
        ClipboardUtils.setClipboardImage(filePathString);
        clickAndSend();
    }
    @Test
    public void test2() throws IOException {
        Image image = ImageIO.read(new URL("http://img.alicdn.com/bao/uploaded/https://img.alicdn.com/imgextra/i2/3998845505/O1CN011BMVJv1qXKq4sdzlY_!!3998845505.jpg"));
        ClipboardUtils.setClipboardImage(image);
        clickAndSend();
    }
    @Test
    public void test3() throws Exception {
        ClipboardUtils.setClipboardText("测试");
        RobotUtils.clickMouse(500,500, -1322, 586);
        RobotUtils.pressKey(500, false, KeyEvent.VK_CONTROL,KeyEvent.VK_V);
        RobotUtils.pressKey(500, false, KeyEvent.VK_SHIFT,KeyEvent.VK_ENTER);

        Image image = ImageIO.read(new URL("http://img.alicdn.com/bao/uploaded/https://img.alicdn.com/imgextra/i2/3998845505/O1CN011BMVJv1qXKq4sdzlY_!!3998845505.jpg"));
        ClipboardUtils.setClipboardImage(image);
        RobotUtils.pressKey(500, false, KeyEvent.VK_CONTROL,KeyEvent.VK_V);
        RobotUtils.pressKey(500, false, KeyEvent.VK_SHIFT,KeyEvent.VK_ENTER);

        ClipboardUtils.setClipboardText("测试");
        RobotUtils.pressKey(500, false, KeyEvent.VK_CONTROL,KeyEvent.VK_V);
        RobotUtils.pressKey(500, false, KeyEvent.VK_SHIFT,KeyEvent.VK_ENTER);

        RobotUtils.pressKey(500, true,KeyEvent.VK_ENTER);
    }
}
