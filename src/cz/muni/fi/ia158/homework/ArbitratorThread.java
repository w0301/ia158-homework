package cz.muni.fi.ia158.homework;

import cz.muni.fi.ia158.homework.DetectLineBehavior.Mode;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class ArbitratorThread extends Thread {
	public static final int TURN_ANGLE = 400;
	public static final int MOTOR_SPEED = 300;
	
	private final ColorSensorThread colorSensor;
	private final TouchSensorThread touchSensor;
	private final RegulatedMotor leftMotor = Motor.B;
	private final RegulatedMotor rightMotor = Motor.A;
	private final TurningHistory turningHistory = new TurningHistory();
	
	private Arbitrator arbitrator;
	private volatile Mode detectLineMode = Mode.InitSearch;
	private volatile int detectLineModeCount = 0;
	
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

	public TurningHistory getTurningHistory() {
		return turningHistory;
	}
	
	public Mode getDetectLineMode() {
		return detectLineMode;
	}

	public void setDetectLineMode(Mode detectLineMode) {
		if (detectLineMode != this.detectLineMode) {
			this.detectLineModeCount = 0;
			this.detectLineMode = detectLineMode;
		}
	}
	
	public int getDetectLineModeCount() {
		return detectLineModeCount;
	}
	
	public void incDetectLineModeCount() {
		detectLineModeCount += 1;
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
