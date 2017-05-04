package cz.muni.fi.ia158.homework;

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
		arbitrator.setDetectLineMode(DetectLineBehavior.Mode.GoAroundSearch);

		arbitrator.getLeftMotor().setSpeed(200);
		arbitrator.getRightMotor().setSpeed(200);

		arbitrator.getLeftMotor().rotate(-450, true);
		arbitrator.getRightMotor().rotate(-450);
		
		arbitrator.getLeftMotor().rotate(DetectLineBehavior.TURN_ANGLE, true);
		arbitrator.getRightMotor().rotate(-DetectLineBehavior.TURN_ANGLE);
	}

	@Override
	public void suppress() {
		
	}
}
