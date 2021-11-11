package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Callable;

import java.util.HashMap;

public class BuiltinCommands {
    public static final HashMap<String, Callable> builtinCommands = new HashMap<>();
    static void addCommand(BuiltInCommand command) {
        builtinCommands.put(command.getCallId(), command);
    }
    static {
        addCommand(AddToCommand.INSTANCE);
        addCommand(AssignCommand.INSTANCE);
        addCommand(ClearGraphCommand.INSTANCE);
        addCommand(CopyInGraphCommand.INSTANCE);
        addCommand(DestroyCommand.INSTANCE);
        addCommand(ErrorCommand.INSTANCE);
        addCommand(ForEachCommand.INSTANCE);
        addCommand(ForkJoinCommand.INSTANCE);
        addCommand(GuidCommand.INSTANCE);
        addCommand(HttpGetCommand.INSTANCE);
        addCommand(HttpPostCommand.INSTANCE);
        addCommand(HttpPutCommand.INSTANCE);
        addCommand(IfCommand.INSTANCE);
        addCommand(LogCommand.INSTANCE);
        addCommand(MapCommand.INSTANCE);
        addCommand(MoveToGraphCommand.INSTANCE);
        addCommand(NewCommand.INSTANCE);
        addCommand(NewGraphCommand.INSTANCE);
        addCommand(NewTransientGraphCommand.INSTANCE);
        addCommand(RemoveFromCommand.INSTANCE);
        addCommand(SerialCommand.INSTANCE);
        addCommand(TimeStampCommand.INSTANCE);
        addCommand(WarningCommand.INSTANCE);
    }
}
