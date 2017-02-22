package org.usfirst.frc.team4585.robot.harve.controller;

import org.usfirst.frc.team4585.robot.harve.model.*;
import org.usfirst.frc.team4585.robot.harve.model.drive.*;
import org.usfirst.frc.team4585.robot.harve.view.*;
import org.usfirst.frc.team4585.robot.harve.model.Shooter;
import org.usfirst.frc.team4585.robot.harve.model.drive.HarvDrive;
import org.usfirst.frc.team4585.robot.harve.model.sensors.HRLV_MaxSonar_EZ_Analog;
import org.usfirst.frc.team4585.robot.harve.model.sensors.Sensors;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HarvOperationController {
	private HarvDrive drive;
	private HarvInput driveInput;
	private HarvInput weaponsInput;
	private SmartDashboard dashboard;
	private Sensors sensors;
	private HRLV_MaxSonar_EZ_Analog sonar;
	private Climber climber;
	private Shooter shooter;
	private Loader loader;
	
	private double magX, magY, magRot;
	private double rotLimit;
	private double time;
	private double millisPerIteration;
	private double distanceToGoal;
	
	private int weaponsTriger, weaponsToggle, weaponsAutoAlign, weaponsClimberToggle, weaponsClimberSpeedToggle;//undefiend buttons incase of useing a different controller.
	private int driveXAxis, driveYAxis, driveZAxis;
	
	private boolean isFastClimber;
	private boolean changeIsFastClimber;
	
	private boolean isShooting;
	private boolean changeIsShooting;
	
	private boolean align;
	private boolean setAlign;
	private boolean isAligned;
	
	public void HarvOperationController(){
		drive = new DefaultDrive(0,1);
		driveInput = new FlightStick(0);
		weaponsInput = new FlightStick(1);
		dashboard = new SmartDashboard();
		sonar = new HRLV_MaxSonar_EZ_Analog(0);
		climber = new Climber(2);
		shooter = new Shooter(3);
		loader = new Loader(4);
		
		magX = 0;
		magY = 0;
		magRot = 0;
		rotLimit = 0;
		time = 0;
		millisPerIteration = 0;
	}
	
	public void start(){
		driveControll();
		weaponsControll();
	}
	
	private void driveControll(){
		driveInput.update();
		this.augmentedDriveControll();
	}
	
	private void weaponsControll(){
		weaponsInput.update();
		if(!(align)){
			updateClimber();
			updateShooter();
			updateLoader();
			checkAutoAlign();
		}else{
			updateAutoAlign();
		}
	}
	
	private void updateShooter(){
		if(weaponsInput.buttonIsPressed(this.weaponsToggle)){//toggles the wheel
			if(isShooting)
				changeIsShooting = false;
			else
				changeIsShooting = true;
		}
		else
			isShooting = changeIsShooting;
		
		if(isShooting){
			shooter.setDistance(distanceToGoal);
			shooter.setIsShooting(true);
		}else{
			shooter.setIsShooting(false);
		}
		
		shooter.update();
	}
	
	private void updateLoader(){
		
		if(weaponsInput.buttonIsPressed(this.weaponsTriger)){
			loader.setIsLoading(true);
		}else{
			loader.setIsLoading(false);
		}
		loader.setSpeed(0.5);
		
		loader.update();
	}
	
	private void updateClimber(){
		if(weaponsInput.buttonIsPressed(this.weaponsClimberSpeedToggle)){//toggles the speed of the climber
			if(this.isFastClimber){
				this.changeIsFastClimber = false;
			}else 
				this.changeIsFastClimber = true;
		}else{
			isFastClimber = changeIsFastClimber;
		}
		
		if(isFastClimber)
			this.climber.setSpeed(1);
		else
			this.climber.setSpeed(0.5);
		
		if(weaponsInput.buttonIsPressed(this.weaponsClimberToggle))
			this.climber.setClimb(true);
		else
			this.climber.setClimb(false);
		climber.update();
	}
	
	private void checkAutoAlign(){
		if(this.weaponsInput.buttonIsPressed(this.weaponsAutoAlign)){
			if(align){
				setAlign = false;
			}else
				setAlign = true;
		}else{
			align = setAlign;
		}
	}
	
	private void updateAutoAlign(){
		
	}
	
	private void augmentedDriveControll(){
		magX = driveInput.getInput(Axis.X);
		magY = driveInput.getAxis(Axis.Y);
		magRot = driveInput.getAxis(Axis.Z);
		if(driveInput.getAxis(Axis.Z) > 0 || driveInput.getAxis(Axis.Z) < 0){
			sensors.reset();
		}
		if(!(driveInput.getAxis(Axis.Z) > 0 || driveInput.getAxis(Axis.Z) < 0)){
			if(sensors.getAngle() > 1){
				magRot = -(sensors.getAngle() + 0.11) * 0.011;
			}else if(sensors.getAngle() < 1){
				magRot = -(sensors.getAngle()+0.11 )* 0.011;
			}
		}
	}

}
