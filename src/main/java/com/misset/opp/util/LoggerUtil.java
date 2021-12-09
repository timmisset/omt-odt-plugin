package com.misset.opp.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Computable;

import java.time.Duration;
import java.time.ZonedDateTime;

public class LoggerUtil {

    public static void runWithLogger(Logger logger, String methodName, Runnable runnable) {
        ZonedDateTime start = ZonedDateTime.now();
        runnable.run();
        ZonedDateTime end = ZonedDateTime.now();
        long millis = Duration.between(start, end).toMillis();
        if (millis > 50) {
            logger.info(methodName + " took " + millis + "ms");
        } else {
            logger.debug(methodName + " took " + millis + "ms");
        }
    }

    public static <T> T computeWithLogger(Logger logger, String methodName, Computable<T> computable) {
        ZonedDateTime start = ZonedDateTime.now();
        T compute = computable.compute();
        ZonedDateTime end = ZonedDateTime.now();
        long millis = Duration.between(start, end).toMillis();
        if (millis > 50) {
            logger.info(methodName + " took " + millis + "ms");
        } else {
            logger.debug(methodName + " took " + millis + "ms");
        }
        return compute;
    }

}
