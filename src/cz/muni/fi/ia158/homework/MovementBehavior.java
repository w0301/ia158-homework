package cz.muni.fi.ia158.homework;

import cz.muni.fi.ia158.homework.DetectLineBehavior.Mode;
import lejos.robotics.subsumption.Behavior;

public class MovementBehavior implements Behavior {
	private final ArbitratorThread arbitrator;
		
	private volatile boolean suppressed = false;
	
	public MovementBehavior(ArbitratorThread arbitrator) {
		this.arbitrator = arbitrator;
	}

	@Override
	public boolean takeControl() {
		return !arbitrator.getColorSensor().isReferenceValue();
	}

	@Override
	public void action() {
		arbitrator.setDetectLineMode(Mode.TurningSearch);
		arbitrator.getTurningHistory().push(arbitrator.getRobotTurningSide());
		
		suppressed = false;
		
		arbitrator.getLeftMotor().setSpeed(arbitrator.MOTOR_SPEED);
		arbitrator.getRightMotor().setSpeed(arbitrator.MOTOR_SPEED);
		
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
