package com.antonjohansson.geotest.test.elasticsearch.model;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**
 * Defines a single search hit.
 * 
 * @param <T> The type of hits.
 */
public class ElasticsearchSearchHit<T>
{
    @SerializedName("_id")
    private String id = "";
    @SerializedName("_score")
    private BigDecimal score;
    @SerializedName("_source")
    private T source;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public BigDecimal getScore()
    {
        return score;
    }

    public void setScore(BigDecimal score)
    {
        this.score = score;
    }

    public T getSource()
    {
        return source;
    }

    public void setSource(T hit)
    {
        this.source = hit;
    }
}
