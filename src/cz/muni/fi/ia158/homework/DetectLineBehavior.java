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
	
	/*public static final int TURN_ANGLE = 360;
	public static final int MOTOR_SPEED = 300;*/
	
	private final static Random RANDOM = new Random();
	
	private final ArbitratorThread arbitrator;
	
	private volatile boolean suppressed = false;
	private TurningSide turn = TurningSide.getRandom();
	private int initSearchMoveBy = 2;
	
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
				/*arbitrator.setRobotTurningSide(turn);
				
				arbitrator.getLeftMotor().rotate(-turn.getInt() * TURN_ANGLE, true);
				arbitrator.getRightMotor().rotate(turn.getInt() * TURN_ANGLE, true);
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;

				turn = turn.getOpposite();
				arbitrator.setRobotTurningSide(turn);
				arbitrator.getLeftMotor().rotate(-turn.getInt() * TURN_ANGLE, true);
				arbitrator.getRightMotor().rotate(turn.getInt() * TURN_ANGLE, true);
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;

				arbitrator.getLeftMotor().rotate(-turn.getInt() * TURN_ANGLE, true);
				arbitrator.getRightMotor().rotate(turn.getInt() * TURN_ANGLE, true);
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;
				
				turn = turn.getOpposite();
				arbitrator.setRobotTurningSide(turn);
				arbitrator.getLeftMotor().rotate(-turn.getInt() * TURN_ANGLE, true);
				arbitrator.getRightMotor().rotate(turn.getInt() * TURN_ANGLE, true);
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;*/
				
				int movedBy = 0;
				while(movedBy < initSearchMoveBy) {
					checkLeftAndRight(turn);
					moveForward(2 * 100);
					movedBy += 2;
				}
				
				rotateRobot(TurningSide.LEFT, arbitrator.TURN_ANGLE);
				movedBy = 0;
				while(movedBy < initSearchMoveBy) {
					checkLeftAndRight(turn);
					moveForward(2 * 100);
					movedBy += 2;
				}
				initSearchMoveBy += 2;

				//TODO pohyb po stvorcovej spirale
			}
			else if (arbitrator.getDetectLineMode() == Mode.GoAroundSearch) {
				/*arbitrator.getLeftMotor().rotate(270, true);
				arbitrator.getRightMotor().rotate(270, true);
				
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;*/
				moveForward(270);

				if (!suppressed) {
					/*arbitrator.getLeftMotor().rotate(-TURN_ANGLE / 4, true);
					arbitrator.getRightMotor().rotate(TURN_ANGLE / 4, true);
					
					while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;*/
					rotateRobot(TurningSide.LEFT, arbitrator.TURN_ANGLE / 4);
				}
			}
			else if(arbitrator.getDetectLineMode() == Mode.TurningSearch){
				turn = arbitrator.getTurningHistory().predictSide();
				checkLeftAndRight(turn);
				
				/*arbitrator.setRobotTurningSide(turn);
				
				arbitrator.getLeftMotor().rotate(-turn.getInt() * TURN_ANGLE, true);
				arbitrator.getRightMotor().rotate(turn.getInt() * TURN_ANGLE, true);
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;

				turn = turn.getOpposite();
				arbitrator.setRobotTurningSide(turn);
				arbitrator.getLeftMotor().rotate(-turn.getInt() * TURN_ANGLE, true);
				arbitrator.getRightMotor().rotate(turn.getInt() * TURN_ANGLE, true);
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;

				arbitrator.getLeftMotor().rotate(-turn.getInt() * TURN_ANGLE, true);
				arbitrator.getRightMotor().rotate(turn.getInt() * TURN_ANGLE, true);
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;
				
				turn = turn.getOpposite();
				arbitrator.setRobotTurningSide(turn);
				arbitrator.getLeftMotor().rotate(-turn.getInt() * TURN_ANGLE, true);
				arbitrator.getRightMotor().rotate(turn.getInt() * TURN_ANGLE, true);
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;*/
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
		/*arbitrator.setRobotTurningSide(startingSide);
		arbitrator.getLeftMotor().rotate(-startingSide.getInt() * TURN_ANGLE, true);
		arbitrator.getRightMotor().rotate(startingSide.getInt() * TURN_ANGLE, true);
		while (!suppressed && arbitrator.getLeftMotor().isMoving() 
				&& arbitrator.getRightMotor().isMoving()) {
			// waiting
		}*/
		rotateRobot(startingSide, arbitrator.TURN_ANGLE);
		
		/*TurningHistory.TurningSide opposite = startingSide.getOpposite();
		arbitrator.setRobotTurningSide(opposite);
		arbitrator.getLeftMotor().rotate(-opposite.getInt() * 2 * TURN_ANGLE, true);
		arbitrator.getRightMotor().rotate(opposite.getInt() * 2 * TURN_ANGLE, true);
		while (!suppressed && arbitrator.getLeftMotor().isMoving() 
				&& arbitrator.getRightMotor().isMoving()) {
			// waiting
		}*/
		rotateRobot(startingSide.getOpposite(), 2 * arbitrator.TURN_ANGLE);
		
		/*arbitrator.setRobotTurningSide(startingSide);
		arbitrator.getLeftMotor().rotate(-startingSide.getInt() * TURN_ANGLE, true);
		arbitrator.getRightMotor().rotate(startingSide.getInt() * TURN_ANGLE, true);
		while (!suppressed && arbitrator.getLeftMotor().isMoving() 
				&& arbitrator.getRightMotor().isMoving()) {
			// waiting
		}*/
		rotateRobot(startingSide, arbitrator.TURN_ANGLE);
	}
}
