package cz.muni.fi.ia158.homework;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;

/*
 * Manages detecting the activation of the touch sensor.
 */
public class TouchSensorThread extends Thread {
	private volatile boolean value = false;
	
	private final EV3TouchSensor sensor = new EV3TouchSensor(SensorPort.S2);
	private final SampleProvider sampleProvider = sensor.getTouchMode();
	
	public TouchSensorThread() {
		setDaemon(true);
	}
	
	public boolean isTouched() {
		return value;
	}
	
	public void waitForReady() {
	}
	
	@Override
	public void run() {
		float[] sample = new float[sampleProvider.sampleSize()];
		
		while (true) {
			sampleProvider.fetchSample(sample, 0);
			value = (sample[0] > 0.9);
		}
	}
}
