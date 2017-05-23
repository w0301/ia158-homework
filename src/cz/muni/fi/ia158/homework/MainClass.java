package cz.muni.fi.ia158.homework;

import lejos.hardware.Button;

public class MainClass {
	public static void main(String[] args) throws InterruptedException {
		final ColorSensorThread colorSensor = new ColorSensorThread();
		colorSensor.start();
		
		final TouchSensorThread touchSensor = new TouchSensorThread();
		touchSensor.start();
		
		final DistanceSensorThread distanceSensor = new DistanceSensorThread();
		distanceSensor.start();
		
		final ArbitratorThread arbitrator = new ArbitratorThread(colorSensor, touchSensor);
		arbitrator.start();
		
		while (true) {
			if ((Button.waitForAnyPress() & Button.ID_ESCAPE) != 0) {
				System.exit(1);
				break;
			}
		}

		arbitrator.join();
	}
}
