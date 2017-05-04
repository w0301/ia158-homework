package cz.muni.fi.ia158.homework;

import lejos.hardware.Sound;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;

public class DistanceSensorThread extends Thread {
	private static final int DEFAULT_BEEP_INTERVAL = 100;
	
	private final EV3IRSensor sensor = new EV3IRSensor(SensorPort.S3);
	private final SampleProvider sampleProvider = sensor.getDistanceMode();
	
	public DistanceSensorThread() {
		setDaemon(true);
	}
	
	@Override
	public void run() {
		float[] sample = new float[sampleProvider.sampleSize()];
		
		long lastBeep = System.currentTimeMillis();
		while (true) {
			sampleProvider.fetchSample(sample, 0);
			
			float value = sample[0];
			long currBeep = System.currentTimeMillis();
			if (lastBeep - currBeep >= (long)(DEFAULT_BEEP_INTERVAL * (value / 50.0f))) {
				Sound.beep();
				lastBeep = currBeep;
			}
		}
	}
}
