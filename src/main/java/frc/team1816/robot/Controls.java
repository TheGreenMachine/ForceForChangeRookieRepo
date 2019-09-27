package frc.team1816.robot;

import com.edinarobotics.utils.gamepad.Gamepad;
import frc.team1816.robot.commands.GamepadArmPosCommand;

public class Controls {
    private static Controls instance;
    private Gamepad gamepad;

    private Controls() {
        gamepad=new Gamepad(0);
        gamepad.diamondDown().whenPressed(new GamepadArmPosCommand());
    }
    public double getThrottle(){
         return gamepad.getLeftY();
    }
    public double getTurn(){
        return gamepad.getRightX();
    }
    public double getArm(){return gamepad.getDPadY()*0.75;}
    public static Controls getInstance(){
        if (instance == null){
            instance = new Controls();
        }
        return instance;
    }
}