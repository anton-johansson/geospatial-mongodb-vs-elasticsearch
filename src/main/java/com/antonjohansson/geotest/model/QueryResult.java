package com.antonjohansson.geotest.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Defines a single query result.
 */
public class QueryResult
{
    private int queryId;
    private BigDecimal longitude = BigDecimal.ZERO;
    private BigDecimal latitude = BigDecimal.ZERO;
    private List<GeospatialDocument> documents = Collections.emptyList();

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

    public List<GeospatialDocument> getDocuments()
    {
        return documents;
    }

    public void setDocuments(List<GeospatialDocument> documents)
    {
        this.documents = documents;
    }
}
