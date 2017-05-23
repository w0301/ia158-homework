package cz.muni.fi.ia158.homework;

import cz.muni.fi.ia158.homework.FindLineBehavior.Mode;
import lejos.robotics.subsumption.Behavior;

/*
 * Behavior class for moving the robot forward when the colored line is present under the sensor.
 */
public class MoveForwardBehavior implements Behavior {
	private final ArbitratorThread arbitrator;
		
	private volatile boolean suppressed = false;
	
	public MoveForwardBehavior(ArbitratorThread arbitrator) {
		this.arbitrator = arbitrator;
	}

	@Override
	public boolean takeControl() {
		return !arbitrator.getColorSensor().isReferenceValue();
	}

	@Override
	public void action() {
		arbitrator.setDetectLineMode(Mode.TurningSearch);
		
		suppressed = false;
		
		arbitrator.getLeftMotor().setSpeed(ArbitratorThread.MOTOR_SPEED);
		arbitrator.getRightMotor().setSpeed(ArbitratorThread.MOTOR_SPEED);
		
		while (!suppressed && !arbitrator.getColorSensor().isReferenceValue()) {
			arbitrator.getLeftMotor().forward();
			arbitrator.getRightMotor().forward();
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
		arbitrator.getLeftMotor().stop(true); 
	    arbitrator.getRightMotor().stop(true);
	}
}
