package cz.muni.fi.ia158.homework;

import java.util.Random;

import cz.muni.fi.ia158.homework.TurningHistory.TurningSide;
import lejos.robotics.subsumption.Behavior;

public class DetectLineBehavior implements Behavior {
	public enum Mode {
		InitSearch,
		TurningSearch,
		GoAroundSearch
	}
	
	private final static Random RANDOM = new Random();
	
	private final ArbitratorThread arbitrator;
	
	private volatile boolean suppressed = false;
	private TurningSide turn = TurningSide.getRandom();
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

		arbitrator.getLeftMotor().setSpeed(arbitrator.MOTOR_SPEED);
		arbitrator.getRightMotor().setSpeed(arbitrator.MOTOR_SPEED);
		
		while (!suppressed) {
			if (arbitrator.getDetectLineMode() == Mode.InitSearch) {
				int movedBy = 0;
				while(movedBy < initSearchMoveBy) {
					moveForward(400);
					checkLeftAndRight(turn);
					movedBy += 1;
				}
				rotateRobot(TurningSide.LEFT, arbitrator.TURN_ANGLE);
				
				movedBy = 0;
				while(movedBy < initSearchMoveBy) {
					moveForward(400);
					checkLeftAndRight(turn);
					movedBy += 1;
				}
				rotateRobot(TurningSide.LEFT, arbitrator.TURN_ANGLE);
				
				initSearchMoveBy += 1;
			}
			else if (arbitrator.getDetectLineMode() == Mode.GoAroundSearch) {
				moveForward(400);
				if (!suppressed) {
					rotateRobot(TurningSide.LEFT, arbitrator.TURN_ANGLE / 4);
				}
			}
			else if(arbitrator.getDetectLineMode() == Mode.TurningSearch){
				turn = arbitrator.getTurningHistory().predictSide();
				checkLeftAndRight(turn);
			}
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
		arbitrator.getLeftMotor().stop(true);
		arbitrator.getRightMotor().stop(true);
	}
	
	private void moveForward(int angle) {
		arbitrator.getLeftMotor().rotate(angle, true);
		arbitrator.getRightMotor().rotate(angle, true);
		while (!suppressed && arbitrator.getLeftMotor().isMoving() 
				&& arbitrator.getRightMotor().isMoving()) {
			// waiting
		}
	}
	
	private void rotateRobot(TurningSide side, int angle) {
		arbitrator.setRobotTurningSide(side);
		arbitrator.getLeftMotor().rotate(-side.getInt() * angle, true);
		arbitrator.getRightMotor().rotate(side.getInt() * angle, true);
		while (!suppressed && arbitrator.getLeftMotor().isMoving() 
				&& arbitrator.getRightMotor().isMoving()) {
			// waiting
		}
	}
	
	private void checkLeftAndRight(TurningSide startingSide) {
		rotateRobot(startingSide, arbitrator.TURN_ANGLE);
		rotateRobot(startingSide.getOpposite(), 2 * arbitrator.TURN_ANGLE);
		rotateRobot(startingSide, arbitrator.TURN_ANGLE);
	}
}
