package com.antonjohansson.geotest.model;

import java.math.BigDecimal;

/**
 * Contains information about a query.
 */
public class QueryInfo
{
    private int queryId;
    private BigDecimal longitude = BigDecimal.ZERO;
    private BigDecimal latitude = BigDecimal.ZERO;

    public int getQueryId()
    {
        return queryId;
    }

    public void setQueryId(int queryId)
    {
        this.queryId = queryId;
    }

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
}
