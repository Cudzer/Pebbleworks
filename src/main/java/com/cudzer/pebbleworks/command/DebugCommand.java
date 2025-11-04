package com.cudzer.pebbleworks.command;

import com.cudzer.pebbleworks.data.registry.ExampleDefinitionManager;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class DebugCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("moddebug")
                .requires(src -> src.hasPermission(2))
                .then(Commands.literal("reload")
                        .executes(ctx -> {
                            ExampleDefinitionManager.clear();
                            ctx.getSource().sendSuccess(() -> Component.literal("Cleared definitions!"), true);
                            return 1;
                        }))
                .then(Commands.literal("info")
                        .executes(ctx -> {
                            ctx.getSource().sendSuccess(() ->
                                    Component.literal("Loaded defs: " + ExampleDefinitionManager.size()), true);
                            return 1;
                        })));
    }
}
