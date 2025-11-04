package com.cudzer.pebbleworks.command;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static com.cudzer.pebbleworks.PebbleworksMod.MODID;

@EventBusSubscriber(modid = MODID)
public class PW_Commands {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        DebugCommand.register(event.getDispatcher());
    }
}

