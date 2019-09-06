package frc.team1816.robot;

import com.edinarobotics.utils.hardware.RobotFactory;
import frc.team1816.robot.subsystems.*;

/**
 * Contains all subsystems of the robot. Follows the singleton pattern.
 */
public class Components {
    private static Components instance;

    public Drivetrain drivetrain;

    private Components() {
        RobotFactory factory = Robot.factory;

        /*if (factory.isImplemented(Birdbeak.NAME)) {
            birdbeak = new Birdbeak();
        }
        if (factory.isImplemented(Climber.NAME)) {
            climber = new Climber();
        }
        if (factory.isImplemented(CargoCollector.NAME)) {
            collector = new CargoCollector();
        }
        if (factory.isImplemented(Drivetrain.NAME)) {
            drivetrain = new Drivetrain();
        }
        if (factory.isImplemented(CargoShooter.NAME)) {
            shooter = new CargoShooter();
        }
        if (factory.isImplemented(LedManager.NAME)) {
            ledManager = new LedManager();
        }
        if (factory.isImplemented(CameraMount.NAME)) {
            shifter = new CameraMount();
        }
    }
    */


        /**
         * Returns the singleton instance of Components. Initializes it if there is no
         * current instance.
         *
         * @return The current singleton instance of Components.
         */
    }
}
