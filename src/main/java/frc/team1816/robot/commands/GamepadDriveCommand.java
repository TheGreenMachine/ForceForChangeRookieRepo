package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Controls;
import frc.team1816.robot.subsystems.Drivetrain;

public class GamepadDriveCommand extends Command {
    private Drivetrain drivetrain;

    public GamepadDriveCommand() {
        drivetrain=Components.getInstance().drivetrain;
        requires(drivetrain);

    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        double throttle=Controls.getInstance().getThrottle();
        double turn=Controls.getInstance().getTurn();
        drivetrain.setPower(throttle+turn,throttle-turn);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
