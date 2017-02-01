package com.bestteam.ex1;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import rp.systems.RobotProgrammingDemo;

public class Part3Wall extends RobotProgrammingDemo implements SensorPortListener {

	private DifferentialPilot pilot;
	private UltrasonicSensor ranger, ranger1;
	private boolean m_bumped = false;
	private float distance, distance1;
	private int bumped_before;

	public Part3Wall() {
		SensorPort.S2.addSensorPortListener(this);
		SensorPort.S3.addSensorPortListener(this);
		pilot = new DifferentialPilot(0.48, 1.4, Motor.C, Motor.A);
		ranger = new UltrasonicSensor(SensorPort.S4);
		ranger1 = new UltrasonicSensor(SensorPort.S1);
	}
	
	
	@Override
	public void run() {
		
//		redirectOutput();
		
		Button.waitForAnyPress();
		
		while(m_run) {
			
			distance = ranger.getDistance();
			distance1 = ranger1.getDistance();
			bumped_before = 0;
			
			
			while((isCloseToWall() && !m_bumped) || isFrontFree()) {
				pilot.travel(1, true);
				while(pilot.isMoving() && m_run && !m_bumped) {}
				distance = ranger.getDistance();
				distance1 = ranger1.getDistance();
				
				if(isTurnSideFree()) {
					break;
				}
			}
			
			pilot.stop();
			
			if(m_bumped) {
				pilot.travel(-0.7);
				pilot.stop();
				bumped_before = 1;
				m_bumped = false;
			}
			
			distance = ranger.getDistance();
			distance1 = ranger1.getDistance();
			
			if(isTurnSideFree()) {
				
				if(isFrontFree()) {
					
					if(bumped_before == 0) {
						pilot.travel(0.5);
						pilot.stop();
						pilot.rotate(-91);
						pilot.stop();
						pilot.travel(2);
						pilot.stop();
					} else {
						pilot.rotate(-91);
						pilot.stop();
						pilot.travel(1.5);
						pilot.stop();
					}
					
				} else {
					pilot.rotate(-91);
					pilot.stop();
					pilot.travel(1.5);
					pilot.stop();
				}
				
			} else if(!isFrontFree()) {
				pilot.rotate(91);
				pilot.stop();
			}
		}
	}


	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		m_bumped = true;
	}
	
	private boolean isCloseToWall() {
//		System.out.println("Close to wall --- " + distance);
		if(distance < 15) {
			return true;
		}
		return false;
	}
	
	private boolean isFrontFree() {
//		System.out.println("Free front --- " + distance1);
		if(distance1 > 15) {
			return true;
		}
		return false;
	}
	
	private boolean isTurnSideFree() {
//		System.out.println("Turn side --- " + distance);
		if(distance > 30) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		Part3Wall robot = new Part3Wall();
		robot.run();
	}
	
}
