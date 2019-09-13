package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.checker.CheckFailException;
import com.edinarobotics.utils.checker.Checkable;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1816.robot.Controls;

public class Drivetrain extends Subsystem implements Checkable {
    private double leftPower;
    private double rightPower;
    private TalonSRX left;
    private TalonSRX right;
    private boolean outputChanged;

    public Drivetrain() {
        left = new TalonSRX(2);
        right = new TalonSRX(7);
        left.setInverted(true);

    }

    public void setPower(double leftPower,double rightPower) {
        this.leftPower=leftPower;
        this.rightPower=rightPower;
        outputChanged=true;
    }

    @Override
    public void periodic() {
        if(outputChanged){
            left.set(ControlMode.PercentOutput,leftPower);
            right.set(ControlMode.PercentOutput,rightPower);
            outputChanged=false;
        }
    }

    @Override
    public boolean check() throws CheckFailException {
        return false;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
