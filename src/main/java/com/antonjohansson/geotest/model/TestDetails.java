package com.antonjohansson.geotest.model;

import static java.util.Collections.emptyList;

import java.util.List;

/**
 * Defines details of a test execution.
 */
public class TestDetails
{
    private long totalExecutionTime;
    private long averageAddTime;
    private long maxAddTime;
    private long minAddTime;
    private long averageQueryTime;
    private long maxQueryTime;
    private long minQueryTime;
    private List<QueryResult> results = emptyList();

    public long getTotalExecutionTime()
    {
        return totalExecutionTime;
    }

    public void setTotalExecutionTime(long totalExecutionTime)
    {
        this.totalExecutionTime = totalExecutionTime;
    }

    public long getAverageAddTime()
    {
        return averageAddTime;
    }

    public void setAverageAddTime(long averageAddTime)
    {
        this.averageAddTime = averageAddTime;
    }

    public long getMaxAddTime()
    {
        return maxAddTime;
    }

    public void setMaxAddTime(long maxAddTime)
    {
        this.maxAddTime = maxAddTime;
    }

    public long getMinAddTime()
    {
        return minAddTime;
    }

    public void setMinAddTime(long minAddTime)
    {
        this.minAddTime = minAddTime;
    }

    public long getAverageQueryTime()
    {
        return averageQueryTime;
    }

    public void setAverageQueryTime(long averageQueryTime)
    {
        this.averageQueryTime = averageQueryTime;
    }

    public long getMaxQueryTime()
    {
        return maxQueryTime;
    }

    public void setMaxQueryTime(long maxQueryTime)
    {
        this.maxQueryTime = maxQueryTime;
    }

    public long getMinQueryTime()
    {
        return minQueryTime;
    }

    public void setMinQueryTime(long minQueryTime)
    {
        this.minQueryTime = minQueryTime;
    }

    public List<QueryResult> getResults()
    {
        return results;
    }

    public void setResults(List<QueryResult> results)
    {
        this.results = results;
    }
}
