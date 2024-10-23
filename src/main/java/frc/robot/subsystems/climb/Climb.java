package frc.robot.subsystems.climb;

import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.SignalLogger;
import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import frc.robot.subsystems.climb.ClimbIO.ClimbIOInputs;
import friarLib3.Utils.DisableSubsystem;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class Climb extends DisableSubsystem
{
    private final ClimbIO climbIO;
    private final ClimbIOInputs climbIOAutoLogged = new ClimbIOInputs();
    private final SysIdRoutine sysIdRoutine;

    public Climb(boolean disabled, ClimbIO climbIO)
    {
        super(disabled);
        this.climbIO = climbIO;
        sysIdRoutine =
            new SysIdRoutine(
                new SysIdRoutine.Config(
                    Volts.of(0.2).per(Seconds.of(1)),
                    Volts.of(6),
                    null,
                    (state) -> SignalLogger.writeString("state", state.toString())
                ),
                new SysIdRoutine.Mechanism(
                    (volts) ->
                        climbIO
                            .getMotor()
                            .setControl(climbIO.getVoltageRequest().withOutput(volts.in(Volts))),
                    null,
                    this
                )
            );
    }

    @Override
    public void periodic()
    {
        super.periodic();
        climbIO.updateInputs(climbIOAutoLogged);
        Logger.processInputs(this.getClass().getSimpleName(), (LoggableInputs) climbIOAutoLogged);
    }

    public Command setPosition(double position) { return this.run(() -> climbIO.setPosition(position * ClimbConstants.gearRatio)); }

    public Command setVoltage(double voltage) { return this.run(() -> climbIO.setVoltage(voltage)).finallyDo(climbIO::off); }

    public Command off() { return this.runOnce(climbIO::off); }

    public Command zero() { return this.runOnce(climbIO::zero); }

    public Command extendClimber() { return setPosition(ClimbConstants.ClimbUpPosition); }

    public Command retractClimber() { return setPosition(ClimbConstants.ClimbDownPosition); }

    public Command sysIDQuasistatic(SysIdRoutine.Direction direction) { return sysIdRoutine.quasistatic(direction); }

    public Command sysIDDynamic(SysIdRoutine.Direction direction) { return sysIdRoutine.dynamic(direction); }

}
