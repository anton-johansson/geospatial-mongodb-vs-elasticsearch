package com.antonjohansson.geotest.test.framework;

import java.util.Collection;

import com.antonjohansson.geotest.model.GeospatialDocument;

/**
 * Defines what the framework requires to run tests.
 */
public interface ITestable
{
    /**
     * Gets the label for this testable instance.
     *
     * @return Returns the label of this instance.
     */
    String getLabel();

    /**
     * Gets the name of the docker image to use.
     *
     * @return Returns the name of the docker image.
     */
    String getDockerImageName();

    /**
     * Gets the port that the docker image exposes for the database.
     * 
     * @return Returns the exposed port.
     */
    int getDockerExposedPort();

    /**
     * Adds the given documents to the database.
     * 
     * @param documents The documents to add.
     * @param port The port that the database is listening on.
     */
    void addDocumentsToDatabase(Collection<GeospatialDocument> documents, int port);
}
