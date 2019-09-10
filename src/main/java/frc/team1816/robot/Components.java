package frc.team1816.robot;

import frc.team1816.robot.subsystems.Drivetrain;

public class Components {
    private static Components instance;
    private Drivetrain drivetrain;
    private Components(){
        drivetrain=new Drivetrain();
    }

    public static Components getInstance(){
        if (instance == null){
            instance = new Components();
        }
        return instance;
    }


}