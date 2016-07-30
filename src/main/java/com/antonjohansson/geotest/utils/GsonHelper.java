package com.antonjohansson.geotest.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

/**
 * Helps with GSON stuff.
 */
public class GsonHelper
{
    // Prevent instantiation
    private GsonHelper()
    {
    }

    /** A deserializer for {@link ZonedDateTime}. */
    public static final JsonDeserializer<ZonedDateTime> ZDT_DESERIALIZER = (value, typeOfT, context) ->
    {
        JsonPrimitive primitive = value.getAsJsonPrimitive();
        try
        {
            if (primitive.isString())
            {
                return ZonedDateTime.parse(primitive.getAsString(), DateTimeFormatter.ISO_ZONED_DATE_TIME);
            }

            if (primitive.isNumber())
            {
                return ZonedDateTime.ofInstant(Instant.ofEpochMilli(primitive.getAsLong()), ZoneOffset.UTC);
            }
        }
        catch (RuntimeException e)
        {
            throw new JsonParseException("Unable to parse ZonedDateTime", e);
        }
        throw new JsonParseException("Unable to parse ZonedDateTime");
    };

    /** A serlializer for {@link ZonedDateTime} that serializes to a UTC string. */
    public static final JsonSerializer<ZonedDateTime> ZDT_SERIALIZER = (src, typeOfSrc, context) ->
    {
        JsonElement element = new JsonPrimitive(src.toString());
        return element;
    };
}
