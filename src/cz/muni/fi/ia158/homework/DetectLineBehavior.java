package cz.muni.fi.ia158.homework;

import cz.muni.fi.ia158.homework.TurningHistory.TurningSide;
import lejos.robotics.subsumption.Behavior;

public class DetectLineBehavior implements Behavior {
	public enum Mode {
		InitSearch,
		TurningSearch,
		GoAroundSearch
	}
	
	private final ArbitratorThread arbitrator;
	
	private volatile boolean suppressed = false;
	private int initSearchMoveBy = 1;
	
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

		arbitrator.getLeftMotor().setSpeed(ArbitratorThread.MOTOR_SPEED);
		arbitrator.getRightMotor().setSpeed(ArbitratorThread.MOTOR_SPEED);
		
		while (!suppressed) {
			arbitrator.incDetectLineModeCount();
			
			if (arbitrator.getDetectLineMode() == Mode.InitSearch) {
				int movedBy = 0;
				while(movedBy < initSearchMoveBy) {
					if (moveForward(400)) return;
					if (checkLeftAndRight(TurningSide.RIGHT)) return;
					movedBy += 1;
				}
				if (rotateRobot(TurningSide.LEFT, ArbitratorThread.TURN_ANGLE)) return;
				
				movedBy = 0;
				while(movedBy < initSearchMoveBy) {
					if (moveForward(400)) return;
					if (checkLeftAndRight(TurningSide.RIGHT)) return;
					movedBy += 1;
				}
				if (rotateRobot(TurningSide.LEFT, ArbitratorThread.TURN_ANGLE)) return;
				
				initSearchMoveBy += 1;
			}
			else if (arbitrator.getDetectLineMode() == Mode.GoAroundSearch) {
				if (moveForward(400)) return;
				if (rotateRobot(TurningSide.LEFT, ArbitratorThread.TURN_ANGLE / 4)) return;
				
				if (arbitrator.getDetectLineModeCount() >= 15) { 
					initSearchMoveBy = 1;
					arbitrator.setDetectLineMode(Mode.InitSearch);
				}
			}
			else if(arbitrator.getDetectLineMode() == Mode.TurningSearch) {
				TurningSide turn = arbitrator.getTurningHistory().predictSide();
				if (checkLeftAndRight(turn)) return;
				if (moveForward(300)) return;
				if (checkLeftAndRight(turn.getOpposite())) return;

				initSearchMoveBy = 1;
				arbitrator.setDetectLineMode(Mode.InitSearch);
			}
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
		arbitrator.getLeftMotor().stop(true);
		arbitrator.getRightMotor().stop(true);
	}
	
	private boolean moveForward(int angle) {
		arbitrator.getLeftMotor().rotate(angle, true);
		arbitrator.getRightMotor().rotate(angle, true);
		while (!suppressed && arbitrator.getLeftMotor().isMoving() 
				&& arbitrator.getRightMotor().isMoving()) {
			// waiting
		}
		return suppressed;
	}
	
	private boolean rotateRobot(TurningSide side, int angle) {
		arbitrator.getLeftMotor().rotate(-side.getInt() * angle, true);
		arbitrator.getRightMotor().rotate(side.getInt() * angle, true);
		while (!suppressed && arbitrator.getLeftMotor().isMoving() 
				&& arbitrator.getRightMotor().isMoving()) {
			// waiting
		}
		return suppressed;
	}
	
	private boolean checkLeftAndRight(TurningSide startingSide) {
		if (rotateRobot(startingSide, ArbitratorThread.TURN_ANGLE)) return true;
		if (rotateRobot(startingSide.getOpposite(), 2 * ArbitratorThread.TURN_ANGLE)) return true;
		if (rotateRobot(startingSide, ArbitratorThread.TURN_ANGLE)) return true;
		return suppressed;
	}
}
