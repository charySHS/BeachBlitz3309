package frc.robot.subsystems.climb;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import friarLib3.Utils.PhoenixUtil;
import friarLib3.Utils.TalonUtil;

public class ClimbIOTalonFX implements ClimbIO
{
    private final TalonFX climbMotor = new TalonFX(ClimbConstants.LeftClimbMotorID);

}
