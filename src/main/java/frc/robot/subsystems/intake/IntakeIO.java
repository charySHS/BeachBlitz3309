package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkLowLevel;
import org.littletonrobotics.junction.AutoLog;

import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.CANSparkFlex;

public interface IntakeIO
{
    @AutoLog
    public static class IntakeIOInputs
    {
        public double intakeMotorVoltage = 0.0;
        public double intakeMotorVelocity = 0.0;
        public double intakeMotorStatorCurrent = 0.0;
        public double intakeMotorSupplyCurrent = 0.0;
        public double intakeMotorTemperature = 0.0;
        public double intakeMotorReferenceSlope = 0.0;

        public double feederMotorVoltage = 0.0;
        public double feederMotorVelocity = 0.0;
        public double feederMotorStatorCurrent = 0.0;
        public double feederMotorSupplyCurrent = 0.0;
        public double feederMotorTemperature = 0.0;
        public double feederMotorReferenceSlope = 0.0;

        public boolean hasCurrentSpiked = false;
    }

    public default void updateInputs(IntakeIOInputs inputs) {}

    public default void setIntakeVoltage(double voltage) {}

    public default void setIntakeVelocity(double velocity) {}

    public default void setFeederVoltage(double voltage) {}

    public default void setFeederVelocity(double velocity) {}

    public default TalonFX getIntakeMotor() { return new TalonFX(0); }

    public default VoltageOut getIntakeVoltageRequest() { return new VoltageOut(0); }

    public default CANSparkFlex getFeederMotor() { return new CANSparkFlex(0, CANSparkLowLevel.MotorType.kBrushless); }

    public default void off() {}

    public default boolean hasCurrentSpiked() { return false; }

}
