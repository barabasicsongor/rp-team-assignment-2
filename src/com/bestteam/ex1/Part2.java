package com.bestteam.ex1;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;
import rp.systems.RobotProgrammingDemo;

public class Part2 extends RobotProgrammingDemo implements SensorPortListener{
	private DifferentialPilot pilot;
	private boolean m_bumped = false;
	
	public Part2(){
		SensorPort.S2.addSensorPortListener(this);
		SensorPort.S3.addSensorPortListener(this);
		pilot = new DifferentialPilot(0.56, 1, Motor.C, Motor.A); //TODO wheel diameter
	}
	
	public static void main(String[] args) {
		Part2 robot = new Part2();
		robot.run();
	}

	@Override
	public void run()  {
		Button.waitForAnyPress();
		
		pilot.forward();
		while(m_run){
			if(m_bumped){
				pilot.stop();
				pilot.travel(-0.05);
				pilot.rotate(180);
				m_bumped = false;
				pilot.stop();
				pilot.travel(0.05);
				pilot.forward();
				continue;
			}
			try{
				Thread.sleep(50);
			}
			catch(InterruptedException e){}
			
		}
		
		pilot.stop();
	}

	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		m_bumped = true;
		
	}

}
