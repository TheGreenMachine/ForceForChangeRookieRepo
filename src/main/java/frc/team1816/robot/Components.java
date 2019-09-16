package frc.team1816.robot;

import frc.team1816.robot.subsystems.Arm;
import frc.team1816.robot.subsystems.Drivetrain;

public class Components {
    private static Components instance;
    public Drivetrain drivetrain;
    public Arm arm;
    private Components() {
        drivetrain = new Drivetrain();
        arm = new Arm();

    }

    public static Components getInstance(){
        if (instance == null){
            instance = new Components();
        }
        return instance;
    }


}