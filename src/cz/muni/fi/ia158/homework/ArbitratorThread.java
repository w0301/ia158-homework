package cz.muni.fi.ia158.homework;

import cz.muni.fi.ia158.homework.TurningHistory.TurningSide;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class ArbitratorThread extends Thread {
	private final ColorSensorThread colorSensor;
	private final TouchSensorThread touchSensor;
	private final RegulatedMotor leftMotor = Motor.B;
	private final RegulatedMotor rightMotor = Motor.A;
	
	private Arbitrator arbitrator;
	private DetectLineBehavior.Mode detectLineMode = DetectLineBehavior.Mode.InitSearch;
	private TurningHistory.TurningSide robotTurningSide = null;
	private TurningHistory turningHistory = new TurningHistory();
	
	public ArbitratorThread(ColorSensorThread colorSensor, TouchSensorThread touchSensor) {
		this.colorSensor = colorSensor;
		this.touchSensor = touchSensor;
	}
	
	public ColorSensorThread getColorSensor() {
		return colorSensor;
	}
	
	public TouchSensorThread getTouchSensor() {
		return touchSensor;
	}
	
	public RegulatedMotor getLeftMotor() {
		return leftMotor;
	}
	
	public RegulatedMotor getRightMotor() {
		return rightMotor;
	}
	
	public DetectLineBehavior.Mode getDetectLineMode() {
		return detectLineMode;
	}

	public void setDetectLineMode(DetectLineBehavior.Mode detectLineMode) {
		this.detectLineMode = detectLineMode;
	}

	public TurningHistory getTurningHistory() {
		return turningHistory;
	}

	public void setTurningHistory(TurningHistory turningHistory) {
		this.turningHistory = turningHistory;
	}
	
	public TurningHistory.TurningSide getRobotTurningSide() {
		return robotTurningSide;
	}

	public void setRobotTurningSide(TurningHistory.TurningSide robotTurningSide) {
		this.robotTurningSide = robotTurningSide;
	}

	@Override
	public void run() {
		try {
			colorSensor.waitForReady();
			touchSensor.waitForReady();
		}
		catch (InterruptedException e) {
			return;
		}
		
		arbitrator = new Arbitrator(new Behavior[] {
			new DetectLineBehavior(this),
			new MovementBehavior(this),
			new DetectWallBehavior(this)
		});
		arbitrator.go();
	}

}
