package com.cudzer.pebbleworks.core;

import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PW_Logger {
    private static final Logger LOGGER = LogManager.getLogger(PW_Constants.NAME);

    public static void info(String msg) { LOGGER.info(msg); }
    public static void warn(String msg) { LOGGER.warn(msg); }
    public static void error(String msg) { LOGGER.error(msg); }
    public static void debug(String msg) {
        if (FMLEnvironment.production) return;
        LOGGER.info("[DEBUG] " + msg);
    }
}
