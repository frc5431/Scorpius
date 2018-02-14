package org.usfirst.frc.team5431.robot.auton; //Change to allow negative distance values

import java.util.ArrayList;

import org.usfirst.frc.team5431.robot.Constants;
import org.usfirst.frc.team5431.robot.Robot;
import org.usfirst.frc.team5431.robot.Titan;
import org.usfirst.frc.team5431.robot.components.DriveBase.TitanPIDSource;
import org.usfirst.frc.team5431.robot.pathfinding.Collector;
import org.usfirst.frc.team5431.robot.pathfinding.Collector.Stepper;
import org.usfirst.frc.team5431.robot.vision.Vision;

public class PathfindingStep extends Step {
	public static enum Paths {
		RIGHT_SCALE
	}
	
	private int currentStep = 0;
	private boolean wasDrive = false;
	private boolean wasTurn = false;
	private final ArrayList<Stepper> steps;
	
	public PathfindingStep(final Paths path) {
		name = "PathfindingStep";
		
		//Collect the path finding file
		Collector.init(path.toString());
		steps = Collector.getData();
		
		properties = String.format("Steps %d", steps.size());
	}
	
	@Override
	public void init(final Robot robot) {
		robot.getDriveBase().setHome();
	}

	@Override
	public StepResult periodic(final Robot robot) {
		boolean nextStep = true;
		try {
			Stepper step = steps.get(currentStep);
			if(!step.isDrive && !step.isTurn) {
				robot.getDriveBase().setHome();
			} else if(step.isDrive && !wasDrive) {
				robot.getDriveBase().driveAtAnglePID(step.drivePower, step.angle, TitanPIDSource.NAVX, Vision.TargetMode.Normal);
				nextStep = false;
			} else if(step.isDrive  && wasDrive) {
				robot.getDriveBase().updateStepResults(step.drivePower, step.angle);
				if(!robot.getDriveBase().hasTravelled(step.distance) && !(Math.abs(step.drivePower) < Constants.AUTO_PATHFINDING_OVERRIDE_NEXT_STEP_SPEED)) {
					Titan.l("Pathfinding is falling behind!");
					nextStep = false;
				}
			} else if(step.isTurn && !wasTurn) {
				robot.getDriveBase().turnPID(step.angle, TitanPIDSource.NAVX, Vision.TargetMode.Normal);
				nextStep = false;
			} else if(step.isTurn && wasTurn) {
				robot.getDriveBase().updateStepResults(step.drivePower, step.angle);
				if(!robot.getDriveBase().hasTurned(step.angle) && !(Math.abs(step.angle) < Constants.AUTO_PATHFINDING_OVERRIDE_NEXT_STEP_TURN)) {
					Titan.l("Pathfinding is falling behind!");
					nextStep = false;
				}
			} else {
				Titan.e("Invalid step command!");
			}
			
			wasDrive = step.isDrive;
			wasTurn = step.isTurn;
		} catch (IndexOutOfBoundsException e) {}
		if(nextStep) if(((currentStep++) + 1) > steps.size()) return StepResult.COMPLETE;
		return StepResult.IN_PROGRESS;
	}

	@Override
	public void done(final Robot robot) {
		robot.getDriveBase().disableAllPID();
		robot.getDriveBase().drive(0.0, 0.0);
	}
}
