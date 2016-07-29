package com.antonjohansson.geotest.test.elasticsearch.model;

/**
 * Result from Elasticsearch.
 * 
 * @param <T> The type of hits.
 */
public class ElasticsearchSearchResult<T>
{
    private ElasticsearchSearchHits<T> hits = new ElasticsearchSearchHits<>();

    public ElasticsearchSearchHits<T> getHits()
    {
        return hits;
    }

    public void setHits(ElasticsearchSearchHits<T> hits)
    {
        this.hits = hits;
    }
}
