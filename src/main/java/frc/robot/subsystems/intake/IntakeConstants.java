package frc.robot.subsystems.intake;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkFlex;

public final class IntakeConstants
{
    /* CAN */
    public static final int IntakeMotorID = 23;
    public static final int FeederMotorID = 10;

    public static final double IntakePower = 1;
    public static final double FeedPower = 0.5;

    public static final double TargetThreshold = 10;

    public static double updateFrequency = 50;
    public static boolean IntakeMotionMagic = false;

    public static final TalonFXConfiguration intakeMotorConfig =
        new TalonFXConfiguration()
            .withSlot0(new Slot0Configs().withKS(40).withKV(0.25).withKP(10).withKI(0).withKD(0))
            .withMotorOutput(
                new MotorOutputConfigs()
                    .withNeutralMode(NeutralModeValue.Brake))
            .withCurrentLimits(
                new CurrentLimitsConfigs()
                    .withStatorCurrentLimitEnable(true)
                    .withStatorCurrentLimit(80));
}
