package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.checker.CheckFailException;
import com.edinarobotics.utils.checker.Checkable;
import com.edinarobotics.utils.hardware.RobotFactory;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1816.robot.Controls;
import frc.team1816.robot.Robot;

public class Drivetrain extends Subsystem implements Checkable {
    public static final String NAME = "drivetrain";
    private double leftPower;
    private double rightPower;

    private IMotorController leftMain;
    private IMotorController leftSlaveOne;
    private IMotorController leftSlaveTwo;

    private IMotorController rightMain;
    private IMotorController rightSlaveOne;
    private IMotorController rightSlaveTwo;
    private boolean outputChanged;

    public Drivetrain() {
        RobotFactory factory= Robot.factory;
        leftMain = factory.getMotor(NAME, "leftMain");
        leftSlaveOne = factory.getMotor(NAME, "leftSlaveOne", leftMain);
        leftSlaveOne = factory.getMotor(NAME, "leftSlaveTwo", leftMain);

        rightMain = factory.getMotor(NAME, "rightMain");
        rightSlaveOne = factory.getMotor(NAME, "rightSlaveOne",rightMain);
        rightSlaveTwo = factory.getMotor(NAME, "rightSlaveTwo",rightMain);
        invertTalons(true);

    }
    private void invertTalons(boolean invertRight) {
        if (invertRight) {
            this.rightMain.setInverted(true);
            this.rightSlaveOne.setInverted(true);
            this.rightSlaveTwo.setInverted(true);
        } else {
            this.leftMain.setInverted(true);
            this.leftSlaveOne.setInverted(true);
            this.leftSlaveTwo.setInverted(true);
        }
    }

    public void setPower(double leftPower,double rightPower) {
        this.leftPower=leftPower;
        this.rightPower=rightPower;
        outputChanged=true;
    }

    @Override
    public void periodic() {
        if(outputChanged){
            leftMain.set(ControlMode.PercentOutput,leftPower);
            rightMain.set(ControlMode.PercentOutput,rightPower);
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
