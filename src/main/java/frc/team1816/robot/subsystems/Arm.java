package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.checker.CheckFailException;
import com.edinarobotics.utils.checker.Checkable;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Arm extends Subsystem implements Checkable {
    public static final int FORWARD_SENSOR_LIMIT = 2590;
    public static final int REVERSE_SENSOR_LIMIT = 1769;
    private static final int ALLOWABLE_CLOSED_LOOP_ERROR = 50;
    private static final int kPIDLoopIdx = 0;
    private static final int kTimeoutMs = 30;
    private double power;
    private TalonSRX arm;
    private boolean outputChanged;
    public Arm(){
        arm=new TalonSRX(4);

        configArm();

        // Calibrate quadrature encoder with absolute mag encoder
        int absolutePosition = arm.getSensorCollection().getPulseWidthPosition();
        /* Mask out overflows, keep bottom 12 bits */
        absolutePosition &= 0xFFF;
        /* Set the quadrature (relative) sensor to match absolute */
        arm.setSelectedSensorPosition(absolutePosition, kPIDLoopIdx, kTimeoutMs);

        outputChanged = true;
    }
    private void configArm(){
        arm.setInverted(true);
        arm.setSensorPhase(true);
        arm.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
                kPIDLoopIdx, kTimeoutMs);
        arm.configAllowableClosedloopError(kPIDLoopIdx, ALLOWABLE_CLOSED_LOOP_ERROR, kTimeoutMs);
        arm.configForwardSoftLimitEnable(true);
        arm.configReverseSoftLimitEnable(true);
        arm.configForwardSoftLimitThreshold(FORWARD_SENSOR_LIMIT,kTimeoutMs);
        arm.configReverseSoftLimitThreshold(REVERSE_SENSOR_LIMIT, kTimeoutMs);
    }
    public void setPower(double power){
        outputChanged=true;
        this.power=power;
    }
    @Override
    public void periodic(){
        if(outputChanged){
            arm.set(ControlMode.PercentOutput,power);
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
