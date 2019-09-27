package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.checker.CheckFailException;
import com.edinarobotics.utils.checker.Checkable;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Arm extends Subsystem implements Checkable {
    private double kF = 0;
    private double kP = 5.0;
    private double kI = 0;
    private double kD = 1.0;

    public static final int FORWARD_SENSOR_LIMIT = 2590;
    public static final int REVERSE_SENSOR_LIMIT = 1769;
    private static final int ALLOWABLE_CLOSED_LOOP_ERROR = 50;
    private static final int kPIDLoopIdx = 0;
    private static final int kTimeoutMs = 30;
    private double power;
    private double position;
    private TalonSRX arm;
    private boolean outputChanged=true;
    private boolean percentMode=false;

    public Arm(){
        arm=new TalonSRX(4);

        configArm();

        // Calibrate quadrature encoder with absolute mag encoder
        int absolutePosition = arm.getSensorCollection().getPulseWidthPosition();
        /* Mask out overflows, keep bottom 12 bits */
        absolutePosition &= 0xFFF;
        /* Set the quadrature (relative) sensor to match absolute */
        arm.setSelectedSensorPosition(absolutePosition, kPIDLoopIdx, kTimeoutMs);

        percentMode = true;
        outputChanged = true;
    }
    private void configArm(){
        arm.setInverted(true);
        arm.setSensorPhase(true);
        arm.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
                kPIDLoopIdx, kTimeoutMs);
        arm.configNominalOutputForward(0,kTimeoutMs);
        arm.configNominalOutputReverse(0,kTimeoutMs);
        arm.configPeakOutputForward(1,kTimeoutMs);
        arm.configPeakOutputReverse(-1,kTimeoutMs);
        this.setPID(kP,kI,kD);
        arm.configMotionAcceleration(19);
        arm.configAllowableClosedloopError(kPIDLoopIdx, ALLOWABLE_CLOSED_LOOP_ERROR, kTimeoutMs);
        arm.configForwardSoftLimitEnable(true);
        arm.configReverseSoftLimitEnable(true);
        arm.configForwardSoftLimitThreshold(FORWARD_SENSOR_LIMIT,kTimeoutMs);
        arm.configReverseSoftLimitThreshold(REVERSE_SENSOR_LIMIT, kTimeoutMs);
    }
    public void setPower(double power){
        percentMode=true;
        outputChanged=true;
        this.power=power;
    }
    public void setPosition(double position){
        System.out.println(position);
        this.position=position;
        percentMode=false;
        outputChanged=true;
    }
    public boolean isBusy() {
        if (arm.getControlMode() == ControlMode.Position) {
            return (arm.getClosedLoopError(kPIDLoopIdx) <= ALLOWABLE_CLOSED_LOOP_ERROR);
        }
        return false;
    }
    public void setPID(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.arm.config_kF(kPIDLoopIdx, kF, kTimeoutMs);
        this.arm.config_kP(kPIDLoopIdx, kP, kTimeoutMs);
        this.arm.config_kI(kPIDLoopIdx, kI, kTimeoutMs);
        this.arm.config_kD(kPIDLoopIdx, kD, kTimeoutMs);
    }
    @Override
    public void periodic(){
        if(outputChanged){
            if(percentMode) {
                arm.set(ControlMode.PercentOutput, power);
            }
            else{
                arm.set(ControlMode.Position,position);
            }
            System.out.println("Arm.power: " + arm.getMotorOutputPercent());
            outputChanged = false;
        }
        System.out.println("Arm.velocity: " + arm.getSelectedSensorVelocity(kPIDLoopIdx));
    }

    @Override
    public boolean check() throws CheckFailException {
        return false;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
