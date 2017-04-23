package cz.muni.fi.ia158.homework;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorSensorThread extends Thread {
	private volatile int value = 0;
	
	private final EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S1);
	private final SampleProvider sampleProvider = sensor.getRedMode();
	
	public ColorSensorThread() {
		setDaemon(true);
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public void run() {
		float[] sample = new float[sampleProvider.sampleSize()];
		
		while (true) {
			sampleProvider.fetchSample(sample, 0);
			value = (int)sample[0];
		}
	}
}
