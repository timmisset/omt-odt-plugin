package com.misset.opp.odt.builtin.commands;

import com.misset.opp.resolvable.Callable;

import java.util.Collection;
import java.util.HashMap;

public class BuiltinCommands {
    private static final HashMap<String, Callable> BUILTIN_COMMANDS = new HashMap<>();

    private BuiltinCommands() {
        // empty constructor
    }

    static void addCommand(AbstractBuiltInCommand command) {
        BUILTIN_COMMANDS.put(command.getCallId(), command);
    }

    static {
        addCommand(AddToCommand.INSTANCE);
        addCommand(AssertCommand.INSTANCE);
        addCommand(AssignCommand.INSTANCE);
        addCommand(ClearGraphCommand.INSTANCE);
        addCommand(CopyInGraphCommand.INSTANCE);
        addCommand(DestroyCommand.INSTANCE);
        addCommand(ErrorCommand.INSTANCE);
        addCommand(ForEachCommand.INSTANCE);
        addCommand(ForkJoinCommand.INSTANCE);
        addCommand(GuidCommand.INSTANCE);
        addCommand(HttpCallCommandDelete.INSTANCE);
        addCommand(HttpCallCommandGet.INSTANCE);
        addCommand(HttpCallCommandPost.INSTANCE);
        addCommand(HttpCallCommandPut.INSTANCE);
        addCommand(IfCommand.INSTANCE);
        addCommand(JsonParseCommand.INSTANCE);
        addCommand(LoadOntologyCommand.INSTANCE);
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

    public static Collection<Callable> getCommands() {
        return BUILTIN_COMMANDS.values();
    }
}
