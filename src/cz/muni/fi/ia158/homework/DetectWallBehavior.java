package cz.muni.fi.ia158.homework;

import cz.muni.fi.ia158.homework.DetectLineBehavior.Mode;
import lejos.robotics.subsumption.Behavior;

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

		arbitrator.getLeftMotor().setSpeed(arbitrator.MOTOR_SPEED);
		arbitrator.getRightMotor().setSpeed(arbitrator.MOTOR_SPEED);

		arbitrator.getLeftMotor().rotate(-450, true);
		arbitrator.getRightMotor().rotate(-450);
		
		arbitrator.getLeftMotor().rotate(arbitrator.TURN_ANGLE, true);
		arbitrator.getRightMotor().rotate(-arbitrator.TURN_ANGLE);
	}

	@Override
	public void suppress() {
		
	}
}
