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
	
	public static final int TURN_ANGLE = 360;
	

	
	private final static Random RANDOM = new Random();
	
	private final ArbitratorThread arbitrator;
	
	private volatile boolean suppressed = false;
	private TurningHistory.TurningSide turn = TurningSide.getRandom();
	
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
		
		
		while (!suppressed) {
			if (arbitrator.getDetectLineMode() == Mode.InitSearch) {
				arbitrator.setRobotTurningSide(turn);
				
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
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;

				//TODO pohyb po stvorcovej spirale
			}
			else if (arbitrator.getDetectLineMode() == Mode.GoAroundSearch) {
				arbitrator.getLeftMotor().rotate(360, true);
				arbitrator.getRightMotor().rotate(360, true);
				
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;

				if (!suppressed) {
					arbitrator.getLeftMotor().rotate(-TURN_ANGLE / 2, true);
					arbitrator.getRightMotor().rotate(TURN_ANGLE / 2, true);
					
					while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;
				}
			}
			else if(arbitrator.getDetectLineMode() == Mode.TurningSearch){
				turn = arbitrator.getTurningHistory().predictSide();
				arbitrator.setRobotTurningSide(turn);
				
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
				while (!suppressed && arbitrator.getLeftMotor().isMoving()) ;

			}
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
		arbitrator.getLeftMotor().stop(true);
		arbitrator.getRightMotor().stop(true);
	}
}
