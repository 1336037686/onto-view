package com.fjut.test;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class RobotViewLook {
	
	public static void main(String[] args) {
		try {
			System.out.println("请输入按钮的位置：(格式：x y time_min)");
			Scanner input = new Scanner(System.in);
			int num = 1;
			int x = input.nextInt();
			int y = input.nextInt();
			int t = input.nextInt();
			
			Robot robot = new Robot();
			System.out.println("ready...");
			robot.delay(3000);	
			System.out.println("start...");
			while(true) {
				robot.mouseMove(x, y);
				robot.delay(500);
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.delay(100);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
				System.out.println("mouse press success " + num++);
				System.out.println("wait " + t + " min");	
				TimeUnit.SECONDS.sleep(t * 62);
			}
		} catch (Exception e) {
			System.out.println("程序出错了！！！\r\n" + e.getMessage());
		}
	}
}
