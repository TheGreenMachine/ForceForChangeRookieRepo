package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.CargoCollector;
import frc.team1816.robot.subsystems.CargoShooter;
import frc.team1816.robot.subsystems.CargoShooter.ArmPosition;

public class SubsystemCargoIntakeResetCommand extends Command {
    private CargoCollector collector;
    private CargoShooter shooter;

    public SubsystemCargoIntakeResetCommand() {
        super("subsystemcargointakeresetcommand");
        collector = Components.getInstance().collector;
        shooter = Components.getInstance().shooter;

        requires(collector);
        requires(shooter);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        collector.setIntake(0.0);
        shooter.setIntake(0.0);
        collector.setArm(false);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }
}
