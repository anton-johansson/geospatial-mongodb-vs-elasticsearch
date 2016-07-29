package com.antonjohansson.geotest.model;

import java.util.Collections;
import java.util.List;

/**
 * Defines a single query result.
 */
public class QueryResult
{
    private int queryId;
    private List<GeospatialDocument> documents = Collections.emptyList();

    public int getQueryId()
    {
        return queryId;
    }

    public void setQueryId(int queryId)
    {
        this.queryId = queryId;
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
