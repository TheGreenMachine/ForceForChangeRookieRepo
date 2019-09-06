package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Drivetrain extends Subsystem {

    private TalonSRX leftDrive;
    private TalonSRX rightDrive;


    public Drivetrain(int leftDrive, int rightDrive) {

        this.leftDrive = new TalonSRX(leftDrive);
        this.rightDrive = new TalonSRX(rightDrive);

        //this.rightDrive.setInverted(true);

    }



    public void setPower(double left, double right) {
        leftDrive.set(ControlMode.PercentOutput, left);
        rightDrive.set(ControlMode.PercentOutput, right);

    }

    @Override
    protected void initDefaultCommand() {

    }
}