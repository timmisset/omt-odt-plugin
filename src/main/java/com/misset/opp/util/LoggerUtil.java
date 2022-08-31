package com.misset.opp.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Computable;

import java.time.Duration;
import java.time.ZonedDateTime;

public class LoggerUtil {

    private LoggerUtil() {
        // empty constructor
    }

    public static void runWithLogger(Logger logger, String methodName, Runnable runnable) {
        ZonedDateTime start = ZonedDateTime.now();
        runnable.run();
        ZonedDateTime end = ZonedDateTime.now();
        doLog(logger, methodName, start, end);
    }

    public static <T> T computeWithLogger(Logger logger, String methodName, Computable<T> computable) {
        ZonedDateTime start = ZonedDateTime.now();
        T compute = computable.compute();
        ZonedDateTime end = ZonedDateTime.now();
        doLog(logger, methodName, start, end);
        return compute;
    }

    private static void doLog(Logger logger,
                              String methodName,
                              ZonedDateTime start,
                              ZonedDateTime end) {
        long millis = Duration.between(start, end).toMillis();
        if (millis > 100) {
            logger.info(methodName + " took " + millis + "ms");
        } else {
            logger.debug(methodName + " took " + millis + "ms");
        }
    }

}
