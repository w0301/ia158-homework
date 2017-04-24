package cz.muni.fi.ia158.homework;

import lejos.robotics.subsumption.Behavior;

public class DetectLineBehavior implements Behavior {
	private final ArbitratorThread arbitrator;
	
	private volatile boolean suppressed = false;
	
	private int angle = 0;
	private boolean turn = true;
	
	public DetectLineBehavior(ArbitratorThread arbitrator) {
		this.arbitrator = arbitrator;
	}
	
	@Override
	public boolean takeControl() {
		return arbitrator.getColorSensor().isReferenceValue();
	}

	@Override
	public void action() {
		suppressed = false;
		
		final int step = 20;
		
		if (turn) {
			arbitrator.getLeftMotor().rotate(-step, true);
			arbitrator.getRightMotor().rotate(step);
			angle += step;
		}
		else {
			arbitrator.getLeftMotor().rotate(step, true);
			arbitrator.getRightMotor().rotate(-step);
			angle += step;
		}
		
		if (angle >= 180) {
			angle = 0;
			turn = !turn;
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
		arbitrator.getLeftMotor().stop(true);
		arbitrator.getRightMotor().stop(true);
	}
}
