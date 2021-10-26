package com.misset.opp.callable.builtIn.commands;

import com.misset.opp.callable.Callable;

import java.util.HashMap;

public class BuiltinCommands {
    public static final HashMap<String, Callable> builtinCommands = new HashMap<>();
    static {
        builtinCommands.put(AddToCommand.INSTANCE.getCallId(), AddToCommand.INSTANCE);
        builtinCommands.put(AssignCommand.INSTANCE.getCallId(), AssignCommand.INSTANCE);
        builtinCommands.put(ClearGraphCommand.INSTANCE.getCallId(), ClearGraphCommand.INSTANCE);
        builtinCommands.put(CopyInGraphCommand.INSTANCE.getCallId(), CopyInGraphCommand.INSTANCE);
        builtinCommands.put(DestroyCommand.INSTANCE.getCallId(), DestroyCommand.INSTANCE);
        builtinCommands.put(ErrorCommand.INSTANCE.getCallId(), ErrorCommand.INSTANCE);
        builtinCommands.put(ForEachCommand.INSTANCE.getCallId(), ForEachCommand.INSTANCE);
        builtinCommands.put(ForkJoinCommand.INSTANCE.getCallId(), ForkJoinCommand.INSTANCE);
        builtinCommands.put(GuidCommand.INSTANCE.getCallId(), GuidCommand.INSTANCE);
        builtinCommands.put(HttpGetCommand.INSTANCE.getCallId(), HttpGetCommand.INSTANCE);
        builtinCommands.put(HttpPostCommand.INSTANCE.getCallId(), HttpPostCommand.INSTANCE);
        builtinCommands.put(HttpPutCommand.INSTANCE.getCallId(), HttpPutCommand.INSTANCE);
        builtinCommands.put(IfCommand.INSTANCE.getCallId(), IfCommand.INSTANCE);
        builtinCommands.put(LogCommand.INSTANCE.getCallId(), LogCommand.INSTANCE);
        builtinCommands.put(MapCommand.INSTANCE.getCallId(), MapCommand.INSTANCE);
        builtinCommands.put(MoveToGraphCommand.INSTANCE.getCallId(), MoveToGraphCommand.INSTANCE);
        builtinCommands.put(NewCommand.INSTANCE.getCallId(), NewCommand.INSTANCE);
        builtinCommands.put(NewGraphCommand.INSTANCE.getCallId(), NewGraphCommand.INSTANCE);
        builtinCommands.put(NewTransientGraphCommand.INSTANCE.getCallId(), NewTransientGraphCommand.INSTANCE);
        builtinCommands.put(RemoveFromCommand.INSTANCE.getCallId(), RemoveFromCommand.INSTANCE);
        builtinCommands.put(SerialCommand.INSTANCE.getCallId(), SerialCommand.INSTANCE);
        builtinCommands.put(TimeStampCommand.INSTANCE.getCallId(), TimeStampCommand.INSTANCE);
        builtinCommands.put(WarningCommand.INSTANCE.getCallId(), WarningCommand.INSTANCE);
    }
}
