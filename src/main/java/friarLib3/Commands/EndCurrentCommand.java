package friarLib3.Commands;

import edu.wpi.first.wpilibj2.command.Command;

public class EndCurrentCommand extends DebugCommandBase {

    Command currentCommand;

    public EndCurrentCommand(Command command)
    {
        currentCommand = command;
        currentCommand.end(true);
    }
}