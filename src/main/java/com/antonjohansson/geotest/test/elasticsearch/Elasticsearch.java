package com.antonjohansson.geotest.test.elasticsearch;

import java.util.Collection;

import com.antonjohansson.geotest.model.GeospatialDocument;
import com.antonjohansson.geotest.test.framework.ITestable;

/**
 * Describes how to performance test Elasticsearch.
 */
public class Elasticsearch implements ITestable
{
    private static final int PORT = 9200;

    @Override
    public String getLabel()
    {
        return "Elasticsearch";
    }

    @Override
    public String getDockerImageName()
    {
        return "elasticsearch";
    }

    @Override
    public int getDockerExposedPort()
    {
        return PORT;
    }

    @Override
    public void addDocumentsToDatabase(Collection<GeospatialDocument> documents, int port)
    {
    }
}
