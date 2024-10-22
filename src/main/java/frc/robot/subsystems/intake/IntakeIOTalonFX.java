package frc.robot.subsystems.intake;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.*;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.*;

import edu.wpi.first.wpilibj.DigitalInput;

import friarLib3.Utils.PhoenixUtil;
import friarLib3.Utils.TalonUtil;

public class IntakeIOTalonFX implements IntakeIO
{
    final DutyCycleOut IntakeRequest = new DutyCycleOut(0);
    private final TalonFX intakeMotor = new TalonFX(IntakeConstants.IntakeMotorID);
    final VelocityVoltage intakeRequest = new VelocityVoltage(0).withSlot(0);
    final MotionMagicVelocityVoltage motionMagicVelocityVoltage =
        new MotionMagicVelocityVoltage(0).withSlot(0);
    private final VoltageOut intakeVoltageReq = new VoltageOut(0);

    private final StatusSignal<Double> intakeMotorVoltage = intakeMotor.getMotorVoltage();
    private final StatusSignal<Double> intakeMotorVelocity = intakeMotor.getVelocity();
    private final StatusSignal<Double> intakeMotorStatorCurrent = intakeMotor.getStatorCurrent();
    private final StatusSignal<Double> intakeMotorSupplyCurrent = intakeMotor.getSupplyCurrent();
    private final StatusSignal<Double> intakeMotorTemperature = intakeMotor.getDeviceTemp();
    private final StatusSignal<Double> intakeMotorReferenceSlope =
        intakeMotor.getClosedLoopReferenceSlope();

    private final CANSparkFlex feederMotor = new CANSparkFlex(IntakeConstants.FeederMotorID, CANSparkLowLevel.MotorType.kBrushless);

    public IntakeIOTalonFX()
    {
        var motorConfig = IntakeConstants.intakeMotorConfig;
        PhoenixUtil.checkErrorAndRetry(() -> intakeMotor.getConfigurator().refresh(motorConfig));
        TalonUtil.applyAndCheckConfiguration(intakeMotor, motorConfig);

        BaseStatusSignal.setUpdateFrequencyForAll(
            IntakeConstants.updateFrequency,
            intakeMotorVoltage,
            intakeMotorVelocity,
            intakeMotorStatorCurrent,
            intakeMotorSupplyCurrent,
            intakeMotorTemperature,
            intakeMotorReferenceSlope
        );

        intakeMotor.optimizeBusUtilization();
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs)
    {
        BaseStatusSignal.refreshAll(
            intakeMotorVoltage,
            intakeMotorVelocity,
            intakeMotorStatorCurrent,
            intakeMotorSupplyCurrent,
            intakeMotorTemperature,
            intakeMotorReferenceSlope
        );

        inputs.intakeMotorVoltage = intakeMotorVoltage.getValueAsDouble();
        inputs.intakeMotorVelocity = intakeMotorVelocity.getValueAsDouble();
        inputs.intakeMotorStatorCurrent = intakeMotorStatorCurrent.getValueAsDouble();
        inputs.intakeMotorSupplyCurrent = intakeMotorSupplyCurrent.getValueAsDouble();
        inputs.intakeMotorTemperature = intakeMotorTemperature.getValueAsDouble();
        inputs.intakeMotorReferenceSlope = intakeMotorReferenceSlope.getValueAsDouble();

        inputs.hasCurrentSpiked = hasCurrentSpiked();
    }

    @Override
    public void setIntakeVoltage(double voltage) { intakeMotor.setVoltage(voltage); }

    @Override
    public void setIntakeVelocity(double velocity) { intakeMotor.setControl(intakeRequest.withVelocity(velocity)); }

    @Override
    public void setFeederVoltage(double voltage) { feederMotor.setVoltage(voltage); }

    @Override
    public void setFeederVelocity(double velocity) { feederMotor.set(velocity); }

    @Override
    public void off()
    {
        intakeMotor.setControl(new NeutralOut());
        feederMotor.getIdleMode();
    }

    @Override
    public boolean hasCurrentSpiked() { return hasCurrentSpiked(); }

    @Override
    public TalonFX getIntakeMotor() { return intakeMotor; }

    @Override
    public VoltageOut getIntakeVoltageRequest() { return intakeVoltageReq; }

    @Override
    public CANSparkFlex getFeederMotor() { return feederMotor; }

}
