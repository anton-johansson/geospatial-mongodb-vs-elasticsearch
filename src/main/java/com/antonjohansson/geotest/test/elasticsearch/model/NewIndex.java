package com.antonjohansson.geotest.test.elasticsearch.model;

import static java.util.Collections.emptyMap;

import java.util.Map;

/**
 * Defines a new index.
 */
public class NewIndex
{
    private Map<String, Mapping> mappings = emptyMap();

    public Map<String, Mapping> getMappings()
    {
        return mappings;
    }

    public void setMappings(Map<String, Mapping> mappings)
    {
        this.mappings = mappings;
    }
}
