package org.usfirst.frc.team4585.robot.harve.controller;

import edu.wpi.first.wpilibj.smartdashboard.*;
import org.usfirst.frc.team4585.robot.harve.model.*;
import org.usfirst.frc.team4585.robot.harve.model.drive.DefaultDrive;
import org.usfirst.frc.team4585.robot.harve.model.drive.HarvDrive;
import org.usfirst.frc.team4585.robot.harve.model.drive.MecanumDrive;
import org.usfirst.frc.team4585.robot.harve.model.sensors.HRLV_MaxSonar_EZ_Analog;
import org.usfirst.frc.team4585.robot.harve.model.sensors.Sensors;
import org.usfirst.frc.team4585.robot.harve.model.autonomous.*;
import org.usfirst.frc.team4585.robot.harve.view.*;

public class HarvController {
	HarvDrive drive;
	HarvInput input;
	HarvAutoController autonomous;
	SmartDashboard dashboard;
	Sensors sensors;
	HRLV_MaxSonar_EZ_Analog sonarSensor;
	
	private double magX, magY, magRot;
	private double rotLimit;
	private double time;
	private double millisPerIteration;
	//rotation variables
	
	
	public HarvController() {
		millisPerIteration = 20;
		drive = new DefaultDrive(0, 1);
		input = new FlightStick(0);
		autonomous = new HarvAutoController();
		dashboard = new SmartDashboard();
		sensors = new Sensors();
		sonarSensor = new HRLV_MaxSonar_EZ_Analog(0, 20480);
		time = 0;
	}

	private void showInformation() {
		
	}

	public void robotInit() {
		time = System.currentTimeMillis();
		input.makeRound(true);
		sensors.calibrateGyro();
	}

	public void autonomous() {
		autonomous.start();
	}

	public void operatorControl() {
		
		if (System.currentTimeMillis() >= time + millisPerIteration) {
			input.update();
			
			magX = input.getInput(Axis.X);
			magY = input.getAxis(Axis.Y);
			magRot = input.getAxis(Axis.Z);
			
			drive.update(magY, magRot);

			time = System.currentTimeMillis();
		}
	}
	
	
	public void test() {
	}

}
