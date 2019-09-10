package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.checker.CheckFailException;
import com.edinarobotics.utils.checker.Checkable;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1816.robot.Controls;

public class Drivetrain extends Subsystem implements Checkable {
    private TalonSRX left;
    private TalonSRX right;

    public Drivetrain() {
        left = new TalonSRX(2);
        right = new TalonSRX(7);
        left.setInverted(true);

    }

    public void setPower(double leftPower,double rightPower) {
        left.set(ControlMode.PercentOutput,leftPower);
        right.set(ControlMode.PercentOutput,rightPower);
    }

    @Override
    public void periodic() {
        double throttle=Controls.getInstance().getThrottle();
        double turn=Controls.getInstance().getTurn();
        setPower(throttle+turn,throttle-turn);

    }

    @Override
    public boolean check() throws CheckFailException {
        return false;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
