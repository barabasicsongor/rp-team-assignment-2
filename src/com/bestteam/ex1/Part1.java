package com.bestteam.ex1;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Part1 implements Runnable {
	
	public static void main(String[] args) {
		Part1 pt1 = new Part1();
		new Thread(pt1).start();
	}

	@Override
	public void run() {
		Button.waitForAnyPress();
		System.out.println("Hello world");
		Button.waitForAnyPress();
	}

}
