package friarLib3.Math;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;

/**
 * Credit to team 364 for this class
 * <a href="https://github.com/Team364/BaseFalconSwerve/blob/main/src/main/java/frc/lib/util/CTREModuleState.java">...</a>
 * <a href="https://www.chiefdelphi.com/t/swerve-modules-flip-180-degrees-periodically-conditionally/393059">...</a>
 */
public class CTREModuleState {

    /**
     * Minimize the change in heading the desired swerve module state would require by potentially
     * reversing the direction the wheel spins. Customized from WPILib's version to include placing
     * in appropriate scope for CTRE onboard control.
     *
     * @param desiredState The desired state.
     * @param currentAngle The current module angle.
     */
    public static SwerveModuleState optimize(SwerveModuleState desiredState, Rotation2d currentAngle)
    {
        double targetAngle = placeInAppropriate0To360Scope(currentAngle.getDegrees(), desiredState.angle.getDegrees());
        double targetSpeed = desiredState.speedMetersPerSecond;
        double delta = targetAngle - currentAngle.getDegrees();
        if (Math.abs(delta) > 90)
        {
            targetSpeed = -targetSpeed;
            targetAngle = delta > 90 ? (targetAngle -=180) : (targetAngle += 180);
        }
        return new SwerveModuleState(targetSpeed, Rotation2d.fromDegrees(targetAngle));
    }
    /**
     * @param scopeReference Current Angle
     * @param newAngle Target Angle
     * @return Closest angle within scope
     */
    public static double placeInAppropriate0To360Scope(double scopeReference, double newAngle)
    {
        double lowerBound;
        double upperBound;
        double lowerOffset = scopeReference % 360;
        if(lowerOffset >= 0)
        {
            lowerBound = scopeReference - lowerOffset;
            upperBound = scopeReference + (360 - lowerOffset);
        }
        else
        {
            upperBound = scopeReference - lowerOffset;
            lowerBound = scopeReference - (360 + lowerOffset);
        }
        while (newAngle < lowerBound)
        {
            newAngle += 360;
        }
        while (newAngle > upperBound)
        {
            newAngle -= 360;
        }
        if (newAngle - scopeReference > 180)
        {
            newAngle -= 360;
        }
        else if (newAngle - scopeReference < -180)
        {
            newAngle += 360;
        }
        return newAngle;
    }
}