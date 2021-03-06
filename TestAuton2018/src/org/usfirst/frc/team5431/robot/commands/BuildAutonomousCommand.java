package org.usfirst.frc.team5431.robot.commands;

import org.usfirst.frc.team5431.robot.Robot;
import org.usfirst.frc.team5431.robot.Titan;
import org.usfirst.frc.team5431.robot.Robot.AutonPriority;
import org.usfirst.frc.team5431.robot.Titan.GameData.FieldObject;
import org.usfirst.frc.team5431.robot.Titan.GameData.SideChooser;
import org.usfirst.frc.team5431.robot.commands.MimicCommand.Paths;
import org.usfirst.frc.team5431.robot.Robot.AutonPosition;

public class BuildAutonomousCommand extends Titan.Command<Robot> {
	private final AutonPriority priority;
	private final AutonPosition position;

	public BuildAutonomousCommand(final AutonPriority pri, final AutonPosition pos) {
		name = "BuildAutonomousCommand";
		properties = String.format("Build the autonomous based off of the game data (Position: %s, Priority: %s)",
				pri.toString(), pos.toString());

		priority = pri;
		position = pos;
	}

	@Override
	public CommandResult update(final Robot robot) {
		return CommandResult.COMPLETE;
	}

	@Override
	public void init(final Robot robot) {
		robot.getGameData().init(); // Capture the current game data
	}

	@Override
	public void done(final Robot robot) {
		final Titan.GameData game = robot.getGameData();
		final Titan.CommandQueue<Robot> aSteps = robot.getAutonSteps();

		final double autonMultiplier = position == AutonPosition.LEFT ? -1 : 1;// if left, -1. if right, 1.
		Titan.l("Auton multiplier is " + autonMultiplier);
		switch (position) {
		case CENTER: {
			switch (priority) {
			case AUTO_LINE:
			case SWITCH_SCALE:
			case SWITCH: 
			case SCALE: {
				game.setSelectedObject(FieldObject.SWITCH);
				// aSteps.add(new DriveStep(60.0));
				game.runSide(new SideChooser() {
					@Override
					public void left() {
						aSteps.add(new MimicCommand(Paths.CENTER_LEFT_SWITCH));
						
						//aSteps.add(new DriveCommand(46.5, 65.0));
						//aSteps.add(new DriveCommand(51, -78));
					}

					@Override
					public void right() {
						aSteps.add(new MimicCommand(Paths.CENTER_RIGHT_SWITCH));
						
						// aSteps.add(new DriveStep(3.0));
						// aSteps.add(new TurnStep(35.0));
						// aSteps.add(new TurnStep(-35.0));
						// aSteps.add(new DriveStep(10.0));
						
						//aSteps.add(new DriveCommand(60.0, -25.0));
						//aSteps.add(new DriveCommand(32.0, 70.0));
					}
				});
				//aSteps.add(new OuttakeCommand(Constants.AUTO_OUTTAKE_TIME));
			}
				break;
			default:
				break;
			}
		}
			break;
		case LEFT:
			switch (priority) {
			case SWITCH_SCALE:
			case SWITCH:
			case SCALE:
				game.setSelectedObject(FieldObject.SWITCH);
				// aSteps.add(new DriveStep(60.0));
				game.runSide(new SideChooser() {
					@Override
					public void left() {
						aSteps.add(new MimicCommand(Paths.LEFT_LEFT_SWITCH));
						
						//aSteps.add(new DriveCommand(46.5, 65.0));
						//aSteps.add(new DriveCommand(51, -78));
					}

					@Override
					public void right() {
						aSteps.add(new DriveCommand(150.0)); //new MimicCommand(Paths.CENTER_RIGHT_SWITCH));
						
						// aSteps.add(new DriveStep(3.0));
						// aSteps.add(new TurnStep(35.0));
						// aSteps.add(new TurnStep(-35.0));
						// aSteps.add(new DriveStep(10.0));
						
						//aSteps.add(new DriveCommand(60.0, -25.0));
						//aSteps.add(new DriveCommand(32.0, 70.0));
					}
				});
				/*if (AutonPosition.valueOf(game.getAllianceSwitch()) == position) {
				aSteps.add(new SwitchAutonomousCommand(game));
				} else if (AutonPosition.valueOf(game.getScale()) == position) {
					aSteps.add(new MimicCommand(Paths.valueOf(position.name() + "_" + game.getAllianceSwitch().name() + "_SWITCH")));
					
					aSteps.add(new ScaleAutonomousCommand(game));
				} else {
					aSteps.add(new DriveCommand(265.0));
				}
				break;*/
				
				/*if (AutonPosition.valueOf(game.getAllianceSwitch()) == position) {
					//aSteps.add(new SwitchAutonomousCommand(game));
					
					//aSteps.add(new MimickCommand(Paths.valueOf(position.name() + "_" + game.getAllianceSwitch().name() + "_SWITCH")));
				}else {
					aSteps.add(new DriveCommand(265.0));
				}*/
				break;
			/*case SCALE:
				if (AutonPosition.valueOf(game.getScale()) == position) {
					aSteps.add(new ScaleAutonomousCommand(game));
				}
				break;*/
			default:
				break;
			}
			break;
		case RIGHT:
			aSteps.add(new DriveCommand(150.0));
			break;
		default:
			break;
		}
	}
}