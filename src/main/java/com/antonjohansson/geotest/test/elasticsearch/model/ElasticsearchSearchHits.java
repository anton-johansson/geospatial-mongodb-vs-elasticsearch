package com.antonjohansson.geotest.test.elasticsearch.model;

import static java.util.Collections.emptyList;

import java.util.List;

/**
 * Result from Elasticsearch.
 * 
 * @param <T> The type of hits.
 */
public class ElasticsearchSearchHits<T>
{
    private List<ElasticsearchSearchHit<T>> hits = emptyList();

    public List<ElasticsearchSearchHit<T>> getHits()
    {
        return hits;
    }

    public void setHits(List<ElasticsearchSearchHit<T>> hits)
    {
        this.hits = hits;
    }
}
