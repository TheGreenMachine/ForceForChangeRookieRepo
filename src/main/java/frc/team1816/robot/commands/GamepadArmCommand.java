package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Controls;
import frc.team1816.robot.subsystems.Arm;
import frc.team1816.robot.subsystems.Drivetrain;

public class GamepadArmCommand extends Command {
    private Arm arm;
    public GamepadArmCommand() {
        arm=Components.getInstance().arm;
        requires(arm);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        double armPower=Controls.getInstance().getArm();
//        if(armPower==1){
//            arm.setPosition(2000);
//        }

        if(!arm.isBusy()||armPower!=0){
            arm.setPower(armPower);
            System.out.println("Command armpower:"+armPower);
        }


    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}