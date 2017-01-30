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
		
		redirectOutput();

		while (m_run) {
			
			distance = ranger.getDistance();
			System.out.println(counter + "---" + distance);
			
//			pilot.forward();
			pilot.travel(5, true);
			while(pilot.isMoving() && !m_bumped) {
				if(m_bumped) {
					pilot.stop();
					pilot.travel(-0.3);
				}
			}
			

			if (counter < 0 && isRightSideFree()) {
				pilot.travel(0.3);
				pilot.stop();
				pilot.rotate(-90);
				pilot.stop();
				counter++;
			} else if(m_bumped) {
				pilot.rotate(90);
				pilot.stop();
				counter--;
				
				m_bumped = false;
			}

			Delay.msDelay(100);
		}

	}

	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		m_bumped = true;
	}

	private boolean isRightSideFree() {
		if(distance > 30) {
//			System.out.println(distance);
			return true;
		}
		return false;
	}

}
