package com.misset.opp.callable.builtIn.commands;

public class DestroyCommand extends BuiltInCommand {
    private DestroyCommand() { }
    public static final DestroyCommand INSTANCE = new DestroyCommand();

    @Override
    public String getName() {
        return "DESTROY";
    }

}
