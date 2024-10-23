package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TorqueCurrentConfigs;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import com.revrobotics.CANSparkFlex;

public class ShooterConstants
{
    /* Misc */
    public static final boolean useShooterMotionMagic = false;
    public static final boolean useFOC = true;

    /* CAN */
    public static int ShooterMotorID = 23;
    public static int FeederMotorID = 10;

    /* PID */
    // Shooter
    public static MotorOutputConfigs motorOutputConfigs =
        new MotorOutputConfigs()
            .withNeutralMode(NeutralModeValue.Brake)
            .withInverted(InvertedValue.Clockwise_Positive);

    // TODO: TUNE ALL THIS YOU GOOFBALL
    public static TalonFXConfiguration ShooterConfigs =
        new TalonFXConfiguration()
            .withSlot0(
                new Slot0Configs()
                    .withKS(0)
                    .withKV(0)
                    .withKP(0)
                    .withKI(0)
                    .withKD(0)
            )
            .withSlot1(new Slot1Configs().withKS(0)
                                         .withKV(0)
                                         .withKP(0)
                                         .withKI(0)
                                         .withKD(0))
            .withMotorOutput(motorOutputConfigs)
            .withMotionMagic(
                new MotionMagicConfigs()
                    .withMotionMagicAcceleration(1600)
                    .withMotionMagicCruiseVelocity(0)
            )
            .withCurrentLimits(
                new CurrentLimitsConfigs()
                    .withStatorCurrentLimitEnable(true)
                    .withStatorCurrentLimit(60)
            )
            .withTorqueCurrent(
                new TorqueCurrentConfigs()
                    .withPeakForwardTorqueCurrent(80)
                    .withPeakReverseTorqueCurrent(80)
            );

    public static double ShooterSpeakerRPS = 42;

    public static double ShooterAmpRPS = 20;

    public static double FeederRPS = 30;

    /* Misc */
    public static double ShooterAngle = 10; // TODO: Tune... The fixed angle for the shooter

    public static double updateFrequency = 50.0;
    public static boolean useMotionMagic = false;

    public static NeutralModeValue neutralMode = NeutralModeValue.Brake;
    public static InvertedValue shooterInverted = InvertedValue.Clockwise_Positive;

    public static double VelocityTolerance = 5;

}
