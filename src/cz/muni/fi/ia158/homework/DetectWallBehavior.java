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

	}

	@Override
	public void suppress() {
		
	}
}
