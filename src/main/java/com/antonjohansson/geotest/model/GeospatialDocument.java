package com.antonjohansson.geotest.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.antonjohansson.geotest.utils.DateUtils;

/**
 * Defines a testing document.
 */
public class GeospatialDocument
{
    private BigDecimal longitude = BigDecimal.ZERO;
    private BigDecimal latitude = BigDecimal.ZERO;
    private ZonedDateTime creationDate = DateUtils.now();
    private String value = "";

    public BigDecimal getLongitude()
    {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude)
    {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude()
    {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude)
    {
        this.latitude = latitude;
    }

    public ZonedDateTime getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate)
    {
        this.creationDate = creationDate;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
