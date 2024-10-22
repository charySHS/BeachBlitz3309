package frc.robot.subsystems.climb;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public final class ClimbConstants
{
    public static final int LeftClimbMotorID = 1;
    public static final double gearRatio = 20; // TODO: tune!

    public static final double ClimbUpPosition = 0.28;
    public static final double ClimbDownPosition = -0.12;

    public static final double ArmTolerance = 15.0 / 360.0;

    private double ManualArmTarget = 0;

    public static double updateFrequency;
    public static boolean UseMotionMagic = false;

    public static final TalonFXConfiguration motorConfig =
        new TalonFXConfiguration()
            .withSlot0(new Slot0Configs().withGravityType(GravityTypeValue.Arm_Cosine).withKS(8).withKV(0).withKP(2000).withKI(0).withKD(65).withKG(27))
            .withMotorOutput(
                new MotorOutputConfigs()
                    .withNeutralMode(NeutralModeValue.Brake)
                    .withInverted(InvertedValue.Clockwise_Positive)
            )
            .withMotionMagic(
                new MotionMagicConfigs()
                    .withMotionMagicAcceleration(0)
                    .withMotionMagicCruiseVelocity(5)
                    .withMotionMagicExpo_kA(3)
                    .withMotionMagicExpo_kV(5)
                    .withMotionMagicJerk(1000)
            )
            .withFeedback(
                new FeedbackConfigs()
                    .withSensorToMechanismRatio(117.6)
            )
            .withSoftwareLimitSwitch(
                new SoftwareLimitSwitchConfigs()
                    .withForwardSoftLimitEnable(true)
                    .withForwardSoftLimitThreshold(ClimbUpPosition)
                    .withReverseSoftLimitEnable(true)
                    .withReverseSoftLimitThreshold(ClimbDownPosition)
            )
            .withCurrentLimits(
                new CurrentLimitsConfigs()
                    .withStatorCurrentLimit(60)
                    .withStatorCurrentLimitEnable(true)
            );
}
