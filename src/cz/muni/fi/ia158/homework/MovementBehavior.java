package cz.muni.fi.ia158.homework;

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
		arbitrator.setDetectLineMode(DetectLineBehavior.Mode.TurningSearch);
		
		suppressed = false;
		
		arbitrator.getLeftMotor().setSpeed(300);
		arbitrator.getRightMotor().setSpeed(300);
		
		while (!suppressed && !arbitrator.getColorSensor().isReferenceValue()) {
			arbitrator.getLeftMotor().forward();
			arbitrator.getRightMotor().forward();
		}

		arbitrator.getLeftMotor().stop(true); 
	    arbitrator.getRightMotor().stop(true);
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
}
