package ru.codein.bw.util;

import lombok.experimental.UtilityClass;
import ru.codein.bw.BedWars;

import java.util.logging.Level;

@UtilityClass
public class Logger {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(BedWars.class.getName());
    private final String prefix = "[BedWars]";
    public static void writeLog(Level level, String message){
        logger.log(level, prefix + " " + message);
    }
}
