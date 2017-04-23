package cz.muni.fi.ia158.homework;

import lejos.robotics.subsumption.Arbitrator;

public class ArbitratorThread extends Thread {
	private final ColorSensorThread colorSensor;
	private final TouchSensorThread touchSensor;
	
	public ArbitratorThread(ColorSensorThread colorSensor, TouchSensorThread touchSensor) {
		this.colorSensor = colorSensor;
		this.touchSensor = touchSensor;
	}
	
	@Override
	public void run() {
		// TODO : prepare arbitrator
		Arbitrator arbitrator;
		
		// TODO : run it
		// arbitrator.go();
	}
}
