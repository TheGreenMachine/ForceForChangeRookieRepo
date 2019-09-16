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
        System.out.println(armPower);
        // arm.setPower(armPower);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
