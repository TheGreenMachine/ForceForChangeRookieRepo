package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;

import com.edinarobotics.utils.checker.CheckFailException;
import com.edinarobotics.utils.checker.Checkable;
import com.edinarobotics.utils.checker.RunTest;
import com.edinarobotics.utils.hardware.RobotFactory;

import com.kauailabs.navx.frc.AHRS;
import com.team254.lib.geometry.Rotation2d;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;

import frc.team1816.robot.Robot;

@RunTest
public class Drivetrain extends Subsystem implements Checkable {
    public static final String NAME = "drivetrain";

    private static double DRIVE_ENCODER_PPR;
    private static double TICKS_PER_REV;
    private static double TICKS_PER_INCH;
    private static double WHEELBASE;
    private static double MAX_VEL_TICKS_PER_100MS;

    private static double INCHES_PER_REV;

    private double kP;
    private double kI;
    private double kD;
    private double kF;

    private AHRS navX;

//    private PigeonIMU gyro;

    private NetworkTable positions = NetworkTableInstance.getDefault().getTable("positions");

    private double initX;
    private double initY;
    private double xPos;
    private double yPos;
    private double prevInches;
    private double prevX;
    private double prevY;
//    private double pigeonAngle;

    private IMotorControllerEnhanced rightMain;
    private IMotorController rightSlaveOne;
    private IMotorController rightSlaveTwo;

    private IMotorControllerEnhanced leftMain;
    private IMotorController leftSlaveOne;
    private IMotorController leftSlaveTwo;

    private double leftPower;
    private double rightPower;

    private double rotation;

    private double leftFeedForward;
    private double rightFeedForward;

    private double leftSetVel;
    private double rightSetVel;

    private Rotation2d gyroAngle;
    private Rotation2d gyroOffset = Rotation2d.identity();

    private double prevLeftInches;
    private double prevRightInches;
    private Rotation2d initAngle;

    private boolean isPercentOut;
    private boolean isSlowMode;
    private boolean isReverseMode;
    private boolean isVisionMode = false;
    private boolean outputsChanged = false;
    private boolean overrideTrajectory = false;


    public Drivetrain() {
        super(NAME);
        RobotFactory factory = Robot.factory;

        DRIVE_ENCODER_PPR = factory.getConstant("driveEncoderPPR");
        TICKS_PER_REV = factory.getConstant("ticksPerRev");
        TICKS_PER_INCH = factory.getConstant("ticksPerIn");
        WHEELBASE = factory.getConstant("wheelbase");
        MAX_VEL_TICKS_PER_100MS = factory.getConstant("maxVel");
        INCHES_PER_REV = TICKS_PER_REV / TICKS_PER_INCH;

        this.leftMain = factory.getMotor(NAME, "leftMain");
        this.leftSlaveOne = factory.getMotor(NAME, "leftSlaveOne", leftMain);
        this.leftSlaveTwo = factory.getMotor(NAME, "leftSlaveTwo", leftMain);

        this.rightMain = factory.getMotor(NAME, "rightMain");
        this.rightSlaveOne = factory.getMotor(NAME, "rightSlaveOne", rightMain);
        this.rightSlaveTwo = factory.getMotor(NAME, "rightSlaveTwo", rightMain);

        try {
            navX = new AHRS(SPI.Port.kMXP);
            System.out.println("NavX Instantiated");
        } catch (RuntimeException e) {
            DriverStation.reportError("Error instantiating navX-MXP:  " + e.getMessage(), true);
        }

    }

    public double getLeftPower() {
        return leftPower;
    }

    public double getRightPower() {
        return rightPower;
    }

    public double getLeftVelocity(){
        return leftSetVel;
    }

    public double getRightVelocity(){
        return rightSetVel;
    }

    public Rotation2d getGyroAngle() {
        return gyroAngle;
    }


    public boolean getGyroStatus() {
        return navX.isConnected();
    }

    public void setDrivetrainVelocity(double leftSetVel, double rightSetVel, double leftFeedForward, double rightFeedForward){
        this.leftFeedForward = leftFeedForward;
        this.rightFeedForward = rightFeedForward;
        setDrivetrainVelocity(leftSetVel, rightSetVel, 0);
    }

    public void setDrivetrainVelocity(double leftSetVel, double rightSetVel) {
        setDrivetrainVelocity(leftSetVel, rightSetVel, 0);
    }

    public void setDrivetrainVelocity(double leftSetVel, double rightSetVel, double rotation) {
        this.leftSetVel = leftSetVel;
        this.rightSetVel = rightSetVel;
        this.rotation = rotation;
        isPercentOut = false;
        outputsChanged = true;
    }

    public void setDrivetrainPercent(double leftPower, double rightPower) {
        setDrivetrainPercent(leftPower, rightPower, 0);
    }

    public void setDrivetrainPercent(double leftPower, double rightPower, double rotation) {
        this.leftPower = leftPower;
        this.rightPower = rightPower;
        this.rotation = rotation;
        isPercentOut = true;
        outputsChanged = true;
    }

    public void setDrivetrainVisionNav(boolean visionOn) {
        isVisionMode = visionOn;
    }

    public boolean getVisionStatus() {
        return isVisionMode;
    }

    public void setSlowMode(boolean slowMode) {
        this.isSlowMode = slowMode;
        outputsChanged = true;
    }

    public boolean isReverseMode() {
        return isReverseMode;
    }

    public void setReverseMode(boolean reverseMode) {
        this.isReverseMode = reverseMode;
        outputsChanged = true;
    }

    public void toggleReverseMode() {
        this.isReverseMode = !this.isReverseMode;
        outputsChanged = true;
    }


    @Override
    public void periodic() {
        this.gyroAngle = Rotation2d.fromDegrees(this.navX.getAngle()).rotateBy(gyroOffset);
        double leftMeasPos = leftMain.getSelectedSensorPosition(0);
        double rightMeasPos = rightMain.getSelectedSensorPosition(0);

        if (outputsChanged) {

            if (isReverseMode) {
                leftPower *= -1;
                rightPower *= -1;
            }

            leftPower += rotation * .55;
            rightPower -= rotation * .55;

            System.out.print("Left Velocity: " + leftSetVel);
            System.out.print("\tRight Velocity: " + rightSetVel);
            System.out.println("\tGyro Angle:" + gyroAngle);

            if (isPercentOut) {
                this.leftMain.set(ControlMode.PercentOutput, leftPower);
                this.rightMain.set(ControlMode.PercentOutput, rightPower);
            } else {
                // this.leftMain.set(ControlMode.Velocity, leftSetVel);
                // this.rightMain.set(ControlMode.Velocity, rightSetVel);

            }

            outputsChanged = false;
        }

    }

    @Override
    protected void initDefaultCommand() {
    }

    @Override
    public boolean check() throws CheckFailException {
        // no-op
        return true;
    }
}
