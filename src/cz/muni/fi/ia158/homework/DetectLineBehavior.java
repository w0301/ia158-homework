package cz.muni.fi.ia158.homework;

import java.util.Random;

import lejos.robotics.subsumption.Behavior;

public class DetectLineBehavior implements Behavior {
	public enum Mode {
		InitSearch,
		TurningSearch,
		GoAroundSearch
	}
	
	public static final int TURN_ANGLE = 360;
	
	private final static Random RANDOM = new Random();
	
	private final ArbitratorThread arbitrator;
	
	private volatile boolean suppressed = false;
	private int turn = 1;
	
	public DetectLineBehavior(ArbitratorThread arbitrator) {
		this.arbitrator = arbitrator;
	}
	
	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		suppressed = false;

		arbitrator.getLeftMotor().setSpeed(300);
		arbitrator.getRightMotor().setSpeed(300);
		
		turn = (RANDOM.nextBoolean() ? 1 : -1);
		
		while (!suppressed) {
			if (arbitrator.getDetectLineMode() == Mode.InitSearch ||
					arbitrator.getDetectLineMode() == Mode.TurningSearch) {
				arbitrator.getLeftMotor().rotate(-turn * TURN_ANGLE, true);
				arbitrator.getRightMotor().rotate(turn * TURN_ANGLE, true);
				
				//arbitrator.getLeftMotor().waitComplete()
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;
	
				arbitrator.getLeftMotor().stop(true);
				arbitrator.getRightMotor().stop(true);

				turn = -turn;
				if (Math.abs(turn) == 1) turn = 2 * turn;
			}
			else if (arbitrator.getDetectLineMode() == Mode.GoAroundSearch) {
				arbitrator.getLeftMotor().rotate(360, true);
				arbitrator.getRightMotor().rotate(360, true);
				
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;

				if (suppressed) {
					arbitrator.getLeftMotor().stop(true);
					arbitrator.getRightMotor().stop(true);	
				}
				else {
					arbitrator.getLeftMotor().rotate(-TURN_ANGLE / 2, true);
					arbitrator.getRightMotor().rotate(TURN_ANGLE / 2, true);
					
					while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;
				}
			}
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
		//arbitrator.getLeftMotor().stop(true);
		//arbitrator.getRightMotor().stop(true);
	}
}
