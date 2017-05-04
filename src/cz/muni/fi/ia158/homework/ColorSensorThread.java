package cz.muni.fi.ia158.homework;

import java.util.concurrent.Semaphore;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorSensorThread extends Thread {
	private volatile float valueR = 0;
	private volatile float valueG = 0;
	private volatile float valueB = 0;
	private volatile float referenceR = 0;
	private volatile float referenceG = 0;
	private volatile float referenceB = 0;
	
	private final Semaphore readySemaphore = new Semaphore(0);
	
	private final EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S1);
	private final SampleProvider sampleProvider = sensor.getRGBMode();
	
	public ColorSensorThread() {
		setDaemon(true);
	}
	
	public boolean isReferenceValue() {
		float r = referenceR - valueR;
		float g = referenceG - valueG;
		float b = referenceB - valueB;
		return Math.sqrt(r*r + g*g + b*b) <= 0.1;
	}
	
	public void waitForReady() throws InterruptedException {
		readySemaphore.acquire();
	}
	
	@Override
	public void run() {
		float[] sample = new float[sampleProvider.sampleSize()];
		
		sampleProvider.fetchSample(sample, 0);
		referenceR = sample[0];
		referenceG = sample[1];
		referenceB = sample[2];
		
		readySemaphore.release();

		while (true) {
			sampleProvider.fetchSample(sample, 0);
			valueR = sample[0];
			valueG = sample[1];
			valueB = sample[2];
		}
	}
}
