package cz.muni.fi.ia158.homework;

import cz.muni.fi.ia158.homework.FindLineBehavior.Mode;
import lejos.robotics.subsumption.Behavior;

/*
 * Behavior class for reacting on pressed touch sensor.
 */
public class DetectWallBehavior implements Behavior {
	private final ArbitratorThread arbitrator;
	
	public DetectWallBehavior(ArbitratorThread arbitrator) {
		this.arbitrator = arbitrator;
	}
	
	@Override
	public boolean takeControl() {
		return arbitrator.getTouchSensor().isTouched();
	}

	@Override
	public void action() {
		arbitrator.setDetectLineMode(Mode.GoAroundSearch);

		arbitrator.getLeftMotor().setSpeed(ArbitratorThread.MOTOR_SPEED);
		arbitrator.getRightMotor().setSpeed(ArbitratorThread.MOTOR_SPEED);

		arbitrator.getLeftMotor().rotate(-450, true);
		arbitrator.getRightMotor().rotate(-450);
		
		arbitrator.getLeftMotor().rotate(ArbitratorThread.TURN_ANGLE, true);
		arbitrator.getRightMotor().rotate(-ArbitratorThread.TURN_ANGLE);
	}

	@Override
	public void suppress() {
		arbitrator.getLeftMotor().stop(true); 
	    arbitrator.getRightMotor().stop(true);
	}
}
