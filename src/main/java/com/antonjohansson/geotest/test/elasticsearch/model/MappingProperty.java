package com.antonjohansson.geotest.test.elasticsearch.model;

/**
 * Defines a single property in a mapping.
 */
public class MappingProperty
{
    private String type = "";

    public MappingProperty()
    {
    }

    public MappingProperty(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
