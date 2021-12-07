package com.misset.opp.util;

import com.intellij.openapi.diagnostic.Logger;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.function.Function;

public class LoggerUtil {

    public static void runWithLogger(Logger logger, Function<Long, String> message, Runnable runnable) {
        ZonedDateTime start = ZonedDateTime.now();
        runnable.run();
        ZonedDateTime end = ZonedDateTime.now();
        long millis = Duration.between(start, end).toMillis();
        logger.info(message.apply(millis));
    }

}
