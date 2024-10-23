package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.CANSparkFlex;

import org.littletonrobotics.junction.AutoLog;

public interface ShooterIO
{
    @AutoLog
    public static class ShooterIOInputs
    {
        public double shooterMotorVoltage = 0.0;
        public double shooterMotorVelocity = 0.0;
        public double shooterMotorStatorCurrent = 0.0;
        public double shooterMotorSupplyCurrent = 0.0;
        public double shooterMotorTemperature = 0.0;
        public double shooterMotorReferenceSlope = 0.0;

        public double feederVoltage = 0.0;
        public double feederVelocity = 0.0;
        public double feederStatorCurrent = 0.0;
        public double feederSupplyCurrent = 0.0;
        public double feederTemperature = 0.0;
        public double feederReferenceSlope = 0.0;
    }

    public default void updateInputs(ShooterIOInputs inputs) {}

    public default void setShooterVoltage(double voltage) {}
}
