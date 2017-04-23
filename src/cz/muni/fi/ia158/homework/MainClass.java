package cz.muni.fi.ia158.homework;

public class MainClass {
	public static void main(String[] args) throws InterruptedException {
		// TODO : show message + wait for any button press to start and esc to stop
		
		final ColorSensorThread colorSensor = new ColorSensorThread();
		colorSensor.start();
		
		final TouchSensorThread touchSensor = new TouchSensorThread();
		touchSensor.start();
		
		final ArbitratorThread arbitrator = new ArbitratorThread(colorSensor, touchSensor);
		arbitrator.start();
		arbitrator.join();
	}
}
