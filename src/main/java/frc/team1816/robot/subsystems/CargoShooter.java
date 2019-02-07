package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.edinarobotics.utils.checker.CheckFailException;
import com.edinarobotics.utils.checker.Checkable;
import com.edinarobotics.utils.hardware.RobotFactory;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1816.robot.Robot;

/**
 * Subsystem for the cargo shooter.
 */
public class CargoShooter extends Subsystem implements Checkable {
    public static final String NAME = "cargoshooter";

    private IMotorControllerEnhanced arm;
    private IMotorController intake;

    private ArmPosition armPosition;

    private double armPositionTicks;
    private double armPower;
    private double intakePower;

    // TODO: Measure true min and max
    public static final int ARM_POSITION_MIN = Robot.factory.getConstant(NAME, "minPos").intValue();
    public static final int ARM_POSITION_MID = Robot.factory.getConstant(NAME, "midPos").intValue();
    public static final int ARM_POSITION_MAX = Robot.factory.getConstant(NAME, "maxPos").intValue();
    private static final int ALLOWABLE_CLOSED_LOOP_ERROR = 50;

    private static final int kPIDLoopIdx = 0;
    private static final int kTimeoutMs = 30;

    private double kP;
    private double kI;
    private double kD;
    private double kF;

    private boolean outputsChanged;
    private boolean isPercentOutput;

    public CargoShooter() {
        super(NAME);
        RobotFactory factory = Robot.factory;

        this.arm = (IMotorControllerEnhanced) factory.getMotor(NAME, "arm");
        this.intake = factory.getMotor(NAME, "intake");
        this.armPower = 0;
        this.intakePower = 0;
        this.outputsChanged = true;
        this.isPercentOutput = true;

        this.kP = factory.getConstant(NAME, "kP");
        this.kI = factory.getConstant(NAME, "kI");
        this.kD = factory.getConstant(NAME, "kD");
        this.kF = factory.getConstant(NAME, "kF");

        configureTalon();

        // Calibrate quadrature encoder with absolute mag encoder
        int absolutePosition = getArmPositionAbsolute();
        /* Mask out overflows, keep bottom 12 bits */
        absolutePosition &= 0xFFF;
        /* Set the quadrature (relative) sensor to match absolute */
        System.out.println("init:\t abs: " + absolutePosition + " rel: " + getArmEncoderPosition());
        this.arm.setSelectedSensorPosition(absolutePosition, kPIDLoopIdx, kTimeoutMs);
        this.armPositionTicks = getArmPositionAbsolute();
        // FIXME: absolutePosition ≠ getArmPositionAbsolute() - potential issue with m
        // sking?

        arm.configOpenloopRamp(2.0, 0); // TODO: tune ramp value
        System.out.println("post:\t abs: " + getArmPositionAbsolute() + " rel: " + getArmEncoderPosition());
    }

    private void configureTalon() {
        arm.setNeutralMode(NeutralMode.Brake);
        arm.setInverted(false);
        arm.setSensorPhase(false);
        arm.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);

        /* Config the peak and nominal outputs, 12V means full */
        arm.configNominalOutputForward(0, kTimeoutMs);
        arm.configNominalOutputReverse(0, kTimeoutMs);
        arm.configPeakOutputForward(1, kTimeoutMs);
        arm.configPeakOutputReverse(-1, kTimeoutMs);

        this.setPid(kP, kI, kD);

        arm.configAllowableClosedloopError(kPIDLoopIdx, ALLOWABLE_CLOSED_LOOP_ERROR, kTimeoutMs);

        // FIXME: testing
        arm.overrideLimitSwitchesEnable(true);
        arm.overrideSoftLimitsEnable(false);

        arm.configForwardSoftLimitEnable(true, kTimeoutMs);
        arm.configReverseSoftLimitEnable(true, kTimeoutMs);
        arm.configForwardSoftLimitThreshold(ARM_POSITION_MAX, kTimeoutMs);
        arm.configReverseSoftLimitThreshold(ARM_POSITION_MIN, kTimeoutMs);
    }

    public void setPid(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        arm.config_kF(kPIDLoopIdx, kF, kTimeoutMs);
        arm.config_kP(kPIDLoopIdx, kP, kTimeoutMs);
        arm.config_kI(kPIDLoopIdx, kI, kTimeoutMs);
        arm.config_kD(kPIDLoopIdx, kD, kTimeoutMs);
    }

    public enum ArmPosition {
        DOWN(ARM_POSITION_MIN), ROCKET(ARM_POSITION_MID), UP(ARM_POSITION_MAX);

        private double armPos;

        ArmPosition(double pos) {
            this.armPos = pos;
        }

        public double getPos() {
            return armPos;
        }
    }

    public void setArmPosition(ArmPosition pos) {
        this.armPosition = pos;
        outputsChanged = true;
        isPercentOutput = false;
    }

    public ArmPosition getArmPosition() {
        return armPosition;
    }

    public int getArmPositionAbsolute() {
        return arm.getSensorCollection().getPulseWidthPosition();
    }

    public double getArmEncoderPosition() {
        return arm.getSelectedSensorPosition(kPIDLoopIdx);
    }

    public boolean isBusy() {
        if (arm.getControlMode() == ControlMode.Position) {
            return (arm.getClosedLoopError(kPIDLoopIdx) <= ALLOWABLE_CLOSED_LOOP_ERROR);
        }
        return false;
    }

    public void setArmPower(double armPow) {
        isPercentOutput = true;
        if (((getArmPositionAbsolute() < ARM_POSITION_MIN) && (armPow < 0))
                || ((getArmPositionAbsolute() > ARM_POSITION_MAX) && (armPow > 0))) {
            System.out.println("Limit hit\tAttempted set: " + armPow + "Arm Pos Abs: " + getArmPositionAbsolute()
                    + "Arm Pos Rel: " + getArmEncoderPosition());
            this.armPower = 0;
            outputsChanged = true;
        } else {
            System.out.println("Nominal range\tSet value: " + armPow + "Arm Pos Abs: " + getArmPositionAbsolute()
                    + "Arm Pos Rel: " + getArmEncoderPosition());
            this.armPower = armPow;
            outputsChanged = true;
        }
    }

    public double getArmPower() {
        return armPower;
    }

    public void setIntake(double intakePow) {
        this.intakePower = intakePow;
        outputsChanged = true;
    }

    public double getIntakePower() {
        return intakePower;
    }

    public boolean isPercentOutput() {
        return isPercentOutput;
    }

    @Override
    public void periodic() {
        if (outputsChanged) {
            if (isPercentOutput) {
                arm.set(ControlMode.PercentOutput, armPower);
            } else {
                System.out.println("Setting Arm to " + armPosition.getPos() + "...");
                arm.set(ControlMode.Position, armPosition.getPos());
            }
            intake.set(ControlMode.PercentOutput, intakePower);
            outputsChanged = false;
        }
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addStringProperty("ControlMode", () -> arm.getControlMode().toString(), null);
        builder.addDoubleProperty("CurrentPosition", this::getArmEncoderPosition, null);
        builder.addDoubleProperty("ClosedLoop/TargetPosition",
                () -> (arm.getControlMode() == ControlMode.Position ? arm.getClosedLoopTarget(kPIDLoopIdx) : 0), null);
        builder.addDoubleProperty("ClosedLoop/Error",
                () -> (arm.getControlMode() == ControlMode.Position ? arm.getClosedLoopError(kPIDLoopIdx) : 0), null);
        builder.addDoubleProperty("MotorOutput", arm::getMotorOutputPercent, null);
        builder.addBooleanProperty("Busy", this::isBusy, null);
        builder.addDoubleProperty("IntakePower", this::getIntakePower, this::setIntake);
        builder.addDoubleProperty("Absolute Arm Position", this::getArmPositionAbsolute, null);
        SmartDashboard.putNumber("max_thresh", ARM_POSITION_MAX);
        SmartDashboard.putNumber("min_thresh", ARM_POSITION_MIN);
    }

    @Override
    public boolean check() throws CheckFailException {
        System.out.println("Warning: mechanisms will move!");
        setArmPosition(ArmPosition.DOWN);
        Timer.delay(5);
        setArmPosition(ArmPosition.UP);
        Timer.delay(5);
        setIntake(1);
        Timer.delay(5);
        setIntake(0);
        Timer.delay(5);
        setArmPosition(ArmPosition.DOWN);
        return true;
    }
}
