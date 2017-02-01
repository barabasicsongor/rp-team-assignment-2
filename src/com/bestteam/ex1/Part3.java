package com.bestteam.ex1;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import rp.systems.RobotProgrammingDemo;

public class Part3 extends RobotProgrammingDemo implements SensorPortListener {

	private DifferentialPilot pilot;
	private UltrasonicSensor ranger;
	private int counter = 0;
	private boolean m_bumped = false;
	private float distance;

	public Part3() {
		SensorPort.S2.addSensorPortListener(this);
		SensorPort.S3.addSensorPortListener(this);
		pilot = new DifferentialPilot(0.48, 1.4, Motor.C, Motor.A);
		ranger = new UltrasonicSensor(SensorPort.S4);
	}

	public static void main(String[] args) {
		Part3 robot = new Part3();
		robot.run();
	}

	@Override
	public void run() {
		Button.waitForAnyPress();
		
//		redirectOutput();
		
		while (m_run) {
			

			pilot.travel(1, true);
			while (pilot.isMoving() && m_run) {

				if (m_bumped) {
					pilot.stop();
					pilot.travel(-1);
				}
			}
			
			distance = ranger.getDistance();

			Delay.msDelay(300);
//			System.out.println("Counter: " + counter + " --- " + distance);
			if (counter < 0 && isRightSideFree()) {
//				System.out.println("Turn right");
				pilot.stop();
				pilot.travel(0.2);
				pilot.stop();
				pilot.rotate(-92);
				pilot.stop();
				
				pilot.travel(3, true);
				while (pilot.isMoving() && m_run) {

					if (m_bumped) {
						pilot.stop();
						pilot.travel(-1);
					}
				}
				
				counter++;
				
				m_bumped = false;
			} else if(m_bumped) {
//				System.out.println("Turn left");
				pilot.rotate(92);
				pilot.stop();
				counter--;
				
				m_bumped = false;
			}
		}

	}

	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		m_bumped = true;
	}

	private boolean isRightSideFree() {
		if(distance > 25) {
//			System.out.println(distance);
			return true;
		}
		return false;
	}

}
