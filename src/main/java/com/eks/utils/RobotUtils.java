package com.eks.utils;

import java.awt.*;
import java.awt.event.InputEvent;

public class RobotUtils {
    private static Robot ROBOT = null;
    static {
        try {
            ROBOT = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    public static void clickMouse(Integer beforeDelayInteger,Integer afterDelayInteger,Integer xInteger,Integer yInteger){
        ROBOT.mouseMove(xInteger, yInteger);
        ROBOT.delay(beforeDelayInteger);
        ROBOT.mousePress(InputEvent.BUTTON1_MASK);
        ROBOT.mouseRelease(InputEvent.BUTTON1_MASK);
        ROBOT.delay(afterDelayInteger);
    }
    public static void pressKey(Integer delayInteger,Boolean oneByoneBoolean,Integer... keyIntegerArray){
        if (oneByoneBoolean) {
            for (Integer keyInt : keyIntegerArray) {
                ROBOT.keyPress(keyInt);
                ROBOT.keyRelease(keyInt);
                ROBOT.delay(delayInteger);
            }
        }else {
            for (Integer keyInt : keyIntegerArray) {
                ROBOT.keyPress(keyInt);
            }
            ROBOT.delay(delayInteger);
            for (Integer keyInt : keyIntegerArray) {
                ROBOT.keyRelease(keyInt);
            }
        }
    }
    public static Point getMousePoint(){
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        return pointerInfo.getLocation();
    }
}
