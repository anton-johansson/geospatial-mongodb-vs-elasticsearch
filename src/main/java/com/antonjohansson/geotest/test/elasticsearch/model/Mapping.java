package com.antonjohansson.geotest.test.elasticsearch.model;

import java.util.Collections;
import java.util.Map;

/**
 * Defines properties of a mapping.
 */
public class Mapping
{
    private Map<String, MappingProperty> properties = Collections.emptyMap();

    public Map<String, MappingProperty> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String, MappingProperty> properties)
    {
        this.properties = properties;
    }
}
