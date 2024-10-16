package frc.robot.subsystems.swerve;

import com.ctre.phoenix6.mechanisms.swerve.utility.PhoenixPIDController;
import com.pathplanner.lib.util.PIDConstants;

import edu.wpi.first.math.controller.PIDController;

public final class SwerveConstants
{
    public static final PhoenixPIDController azimuthController =
        new PhoenixPIDController(8, 0, 0); // Should not have I or D; TODO: Tune
    public static final PIDConstants autoRotationalController =
        new PIDConstants(6, azimuthController.getI(), azimuthController.getD()); // 6
    public static final PIDConstants autoTranslationalController = new PIDConstants(6, 0, 0); // 8

    public static final double azimuthEpsilon = 10.0; // TODO: Tune

    public static final PIDController choreoTranslationController = new PIDController(6, 0, 0);
    public static final PIDController choreoRotationController =
        new PIDController(
            azimuthController.getP(), azimuthController.getI(), azimuthController.getD()
        );
}
