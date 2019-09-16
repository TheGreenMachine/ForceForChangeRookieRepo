package frc.team1816.robot;

import com.edinarobotics.utils.hardware.RobotFactory;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team1816.robot.commands.GamepadArmCommand;
import frc.team1816.robot.commands.GamepadDriveCommand;

public class Robot extends TimedRobot {

    public static final RobotFactory factory = new RobotFactory(
            System.getenv("ROBOT_NAME") != null ? System.getenv("ROBOT_NAME") : "zenith");

    @Override
    public void robotInit() {
        Controls.getInstance();
        Components.getInstance();

    }

    @Override
    public void disabledInit() { }

    @Override
    public void autonomousInit() { }

    @Override
    public void teleopInit() {
        Components.getInstance().drivetrain.setDefaultCommand(new GamepadDriveCommand());
        Components.getInstance().arm.setDefaultCommand(new GamepadArmCommand());
    }

    @Override
    public void testInit() { }


    @Override
    public void disabledPeriodic() { }

    @Override
    public void autonomousPeriodic() { }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void testPeriodic() { }
}