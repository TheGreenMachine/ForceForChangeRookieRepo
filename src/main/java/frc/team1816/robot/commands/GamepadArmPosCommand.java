package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Controls;
import frc.team1816.robot.subsystems.Arm;

public class GamepadArmPosCommand extends Command {
    private Arm arm;
    public GamepadArmPosCommand() {
        arm=Components.getInstance().arm;
        requires(arm);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        arm.setPosition(2000);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
