package com.antonjohansson.geotest.utils;

import static java.math.RoundingMode.HALF_UP;
import static java.time.temporal.ChronoUnit.MILLIS;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Random;

/**
 * Provides utility for managing randomly generated values.
 */
public class RandomGenerator
{
    public static final int COORDINATE_SCALE = 7;
    private static final BigDecimal LONGITUDE_BOUNDARY = new BigDecimal(180);
    private static final BigDecimal LATITUDE_BOUNDARY = new BigDecimal(90);
    private static final BigDecimal MULTIPLIER = new BigDecimal(2);
    private static final int TWO_WEEKS_IN_MILLISECONDS = 60 * 60 * 24 * 14 * 1000;
    private static final int VALUE_LENGTH = 20;
    private static final String VALUE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final Random random;

    /**
     * Constructs a new {@link RandomGenerator} with a seed likely to be unique.
     */
    public RandomGenerator()
    {
        this(new Random());
    }

    /**
     * Constructs a new {@link RandomGenerator} with the given {@code seed}.
     * <p>
     * This is very useful when consistency between tests are desired.
     *
     * @param seed The seed to use.
     */
    public RandomGenerator(long seed)
    {
        this(new Random(seed));
    }

    /**
     * Constructs a new {@link RandomGenerator} with the given {@link Random} instance.
     *
     * @param random The random instance to use.
     */
    public RandomGenerator(Random random)
    {
        this.random = random;
    }

    /**
     * Gets a random longitude value.
     *
     * @return Returns a random longitude value.
     */
    public BigDecimal getLongitude()
    {
        double multiplier = random.nextDouble();
        return new BigDecimal(multiplier)
                .setScale(COORDINATE_SCALE, HALF_UP)
                .multiply(MULTIPLIER)
                .multiply(LONGITUDE_BOUNDARY)
                .subtract(LONGITUDE_BOUNDARY);
    }

    /**
     * Gets a random latitude value.
     *
     * @return Returns a random latitude value.
     */
    public BigDecimal getLatitude()
    {
        double multiplier = random.nextDouble();
        return new BigDecimal(multiplier)
                .setScale(COORDINATE_SCALE, HALF_UP)
                .multiply(MULTIPLIER)
                .multiply(LATITUDE_BOUNDARY)
                .subtract(LATITUDE_BOUNDARY);
    }

    /**
     * Gets a random creation date, between <b>now</b> and <b>two weeks</b> back.
     *
     * @return Returns a random creation date.
     */
    public ZonedDateTime getCreationDate()
    {
        int milliseconds = random.nextInt(TWO_WEEKS_IN_MILLISECONDS);
        return DateUtils.now().plus(-milliseconds, MILLIS);
    }

    /**
     * Gets a random string value.
     */
    public String getValue()
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < VALUE_LENGTH; i++)
        {
            int index = random.nextInt(VALUE_CHARACTERS.length());
            char character = VALUE_CHARACTERS.charAt(index);
            builder.append(character);
        }
        return builder.toString();
    }
}
