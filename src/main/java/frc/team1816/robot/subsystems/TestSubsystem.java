package frc.team1816.robot.subsystems;

import com.edinarobotics.utils.checker.CheckFailException;
import com.edinarobotics.utils.checker.Checkable;
import edu.wpi.first.wpilibj.command.Subsystem;

public class TestSubsystem extends Subsystem implements Checkable {




    @Override
    public boolean check() throws CheckFailException {
        return false;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
