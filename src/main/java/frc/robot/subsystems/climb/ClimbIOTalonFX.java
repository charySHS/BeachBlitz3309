package frc.robot.subsystems.climb;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.*;
import com.ctre.phoenix6.hardware.TalonFX;

import friarLib3.Utils.PhoenixUtil;
import friarLib3.Utils.TalonUtil;

public class ClimbIOTalonFX implements ClimbIO
{
    private final TalonFX secondMotor = new TalonFX(ClimbConstants.RightClimbMotorID);
    private final TalonFX climbMotor = new TalonFX(ClimbConstants.LeftClimbMotorID);
    private final PositionVoltage positionRequest = new PositionVoltage(0).withSlot(0);
    private final MotionMagicExpoTorqueCurrentFOC motionMagicRequest = new MotionMagicExpoTorqueCurrentFOC(0).withSlot(0);
    private final VoltageOut voltageReq = new VoltageOut(0);
    private final PositionTorqueCurrentFOC climbRequest = new PositionTorqueCurrentFOC(0).withSlot(0);

    private final StatusSignal<Double> climbMotorVoltage = climbMotor.getMotorVoltage();
    private final StatusSignal<Double> climbMotorVelocity = climbMotor.getVelocity();
    private final StatusSignal<Double> climbMotorPosition = climbMotor.getPosition();
    private final StatusSignal<Double> climbMotorStatorCurrent = climbMotor.getStatorCurrent();
    private final StatusSignal<Double> climbMotorSupplyCurrent = climbMotor.getSupplyCurrent();
    private final StatusSignal<Double> climbMotorTemperature = climbMotor.getDeviceTemp();
    private final StatusSignal<Double> climbMotorReferenceSlope =
        climbMotor.getClosedLoopReferenceSlope();

    public ClimbIOTalonFX()
    {
        var motorConfig = ClimbConstants.motorConfig;
        PhoenixUtil.checkErrorAndRetry(() -> climbMotor.getConfigurator().refresh(motorConfig));
        TalonUtil.applyAndCheckConfiguration(climbMotor, motorConfig);

        BaseStatusSignal.setUpdateFrequencyForAll(
            ClimbConstants.updateFrequency,
            climbMotorVoltage,
            climbMotorVelocity,
            climbMotorPosition,
            climbMotorStatorCurrent,
            climbMotorSupplyCurrent,
            climbMotorTemperature,
            climbMotorReferenceSlope
        );
        climbMotor.optimizeBusUtilization();

        secondMotor.setControl(new Follower(ClimbConstants.LeftClimbMotorID, true));
    }

    @Override
    public void updateInputs(ClimbIOInputs inputs)
    {
        BaseStatusSignal.refreshAll(
            climbMotorVoltage,
            climbMotorVelocity,
            climbMotorPosition,
            climbMotorStatorCurrent,
            climbMotorSupplyCurrent,
            climbMotorTemperature,
            climbMotorReferenceSlope
        );

        inputs.climbMotorVoltage = climbMotorVoltage.getValueAsDouble();
        inputs.climbMotorVelocity = climbMotorVelocity.getValueAsDouble();
        inputs.climbMotorPosition = climbMotorPosition.getValueAsDouble();
        inputs.climbMotorStatorCurrent = climbMotorStatorCurrent.getValueAsDouble();
        inputs.climbMotorTemperature = climbMotorTemperature.getValueAsDouble();
        inputs.climbMotorReferenceSlope = climbMotorReferenceSlope.getValueAsDouble();
    }

    @Override
    public void setPosition(double position)
    {
        if (ClimbConstants.UseMotionMagic)
        {
            climbMotor.setControl(motionMagicRequest.withPosition(position));
        }
        else
        {
            climbMotor.setControl(climbRequest.withPosition(position));
        }
    }

    @Override
    public void setVoltage(double voltage) { climbMotor.setVoltage(voltage); }

    @Override
    public void off() { climbMotor.setControl(new NeutralOut()); }

    @Override
    public void zero() { climbMotor.setPosition(0); } // TODO: Tune!!!

    @Override
    public TalonFX getMotor()
    {
        return climbMotor;
    }

    @Override public PositionTorqueCurrentFOC getClimbRequest() { return climbRequest; }

}
