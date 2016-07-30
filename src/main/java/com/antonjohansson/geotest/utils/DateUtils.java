package com.antonjohansson.geotest.utils;

import static java.time.ZoneOffset.UTC;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Contains utilities for managing dates.
 */
public final class DateUtils
{
    // Prevent instantiation
    private DateUtils()
    {
    }

    /**
     * Gets the current date and time, in UTC.
     *
     * @return Returns the current date and time.
     */
    public static ZonedDateTime now()
    {
        return ZonedDateTime.now(UTC);
    }

    /**
     * Gets the current date (without time), in UTC.
     *
     * @return Returns the current date.
     */
    public static ZonedDateTime nowWithoutTime()
    {
        return now().toLocalDate().atStartOfDay(ZoneOffset.UTC);
    }
}
