package cz.muni.fi.ia158.homework;

import java.util.concurrent.Semaphore;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorSensorThread extends Thread {
	private volatile float value = 0;
	private volatile float referenceValue = 0;
	
	private final Semaphore readySemaphore = new Semaphore(0);
	
	private final EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S1);
	private final SampleProvider sampleProvider = sensor.getRedMode();
	
	public ColorSensorThread() {
		setDaemon(true);
	}
	
	public float getValue() {
		return value;
	}
	
	public float getReferenceValue() {
		return referenceValue;
	}
	
	public boolean isReferenceValue() {
		return (Math.abs(value - referenceValue) / 1.0f) <= 0.2f;
	}
	
	public void waitForReady() throws InterruptedException {
		readySemaphore.acquire();
	}
	
	@Override
	public void run() {
		float[] sample = new float[sampleProvider.sampleSize()];
		
		sampleProvider.fetchSample(sample, 0);
		referenceValue = sample[0]; 
		
		readySemaphore.release();

		while (true) {
			sampleProvider.fetchSample(sample, 0);
			value = sample[0];
		}
	}
}
