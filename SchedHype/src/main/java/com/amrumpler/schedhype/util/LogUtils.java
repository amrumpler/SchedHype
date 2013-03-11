package com.amrumpler.schedhype.util;

import org.slf4j.Logger;

/**
 * <p>
 * A utility class that will prepend any log messages with the username of the currently logged in user, and a counter
 * that is unique at the request level. This permits the logs to be scanned for all messages that occur for a given user
 * throughout the entirety of the request
 * </p>
 */
public final class LogUtils {

    private LogUtils() {
        // Utility class, so do not allow instantiation
    }

    /**
     * Stores the user name and sequence id for the duration of the request (see
     * {@link com.adt.selfservice.filters.LogFilter LogFilter}). The decision to use ThreadLocal as opposed to using
     * Guice was so that any class can use tagged logging, and not just the classes bound to Guice. It also makes tagged
     * logging decidedly easier to adopt (negating the need for injecting this utility class everywhere).
     */
    public static final ThreadLocal<String> TRACKING_PREFIX = new ThreadLocal<String>();

    public static void trace(Logger log, String message) {
        log.trace(tag(message));
    }

    public static void trace(Logger log, String messageFormat, Object argument) {
        log.trace(tag(messageFormat), argument);
    }

    public static void trace(Logger log, String messageFormat, Object argument1, Object argument2) {
        log.trace(tag(messageFormat), argument1, argument2);
    }

    public static void trace(Logger log, String messageFormat, Object... argumentArray) {
        log.trace(tag(messageFormat), argumentArray);
    }

    public static void trace(Logger log, String message, Throwable throwable) {
        log.trace(tag(message), throwable);
    }

    public static void debug(Logger log, String message) {
        log.debug(tag(message));
    }

    public static void debug(Logger log, String messageFormat, Object argument) {
        log.debug(tag(messageFormat), argument);
    }

    public static void debug(Logger log, String messageFormat, Object argument1, Object argument2) {
        log.debug(tag(messageFormat), argument1, argument2);
    }

    public static void debug(Logger log, String messageFormat, Object... argumentArray) {
        log.debug(tag(messageFormat), argumentArray);
    }

    public static void debug(Logger log, String message, Throwable throwable) {
        log.debug(tag(message), throwable);
    }

    public static void info(Logger log, String message) {
        log.info(tag(message));
    }

    public static void info(Logger log, String messageFormat, Object argument) {
        log.info(tag(messageFormat), argument);
    }

    public static void info(Logger log, String messageFormat, Object argument1, Object argument2) {
        log.info(tag(messageFormat), argument1, argument2);
    }

    public static void info(Logger log, String messageFormat, Object... argumentArray) {
        log.info(tag(messageFormat), argumentArray);
    }

    public static void info(Logger log, String message, Throwable throwable) {
        log.info(tag(message), throwable);
    }

    public static void warn(Logger log, String message) {
        log.warn(tag(message));
    }

    public static void warn(Logger log, String messageFormat, Object argument) {
        log.warn(tag(messageFormat), argument);
    }

    public static void warn(Logger log, String messageFormat, Object argument1, Object argument2) {
        log.warn(tag(messageFormat), argument1, argument2);
    }

    public static void warn(Logger log, String messageFormat, Object... argumentArray) {
        log.warn(tag(messageFormat), argumentArray);
    }

    public static void warn(Logger log, String message, Throwable throwable) {
        log.warn(tag(message), throwable);
    }

    public static void error(Logger log, String message) {
        log.error(tag(message));
    }

    public static void error(Logger log, String messageFormat, Object argument) {
        log.error(tag(messageFormat), argument);
    }

    public static void error(Logger log, String messageFormat, Object argument1, Object argument2) {
        log.error(tag(messageFormat), argument1, argument2);
    }

    public static void error(Logger log, String messageFormat, Object... argumentArray) {
        log.error(tag(messageFormat), argumentArray);
    }

    public static void error(Logger log, String message, Throwable throwable) {
        log.error(tag(message), throwable);
    }

    private static String tag(String message) {
        if (TRACKING_PREFIX.get() == null) {
            return message;
        }
        return new StringBuilder(TRACKING_PREFIX.get()).append(message).toString();
    }
}
