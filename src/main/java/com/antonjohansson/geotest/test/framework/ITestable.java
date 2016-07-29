package com.antonjohansson.geotest.test.framework;

import com.antonjohansson.geotest.model.GeospatialDocument;
import com.antonjohansson.geotest.model.QueryInfo;
import com.antonjohansson.geotest.model.QueryResult;

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
     * Prepares the database.
     *
     * @param port The port that the database is listening on.
     */
    void prepare(int port);

    /**
     * Adds the given document to the database.
     *
     * @param document The document to add.
     * @param port The port that the database is listening on.
     */
    void addDocumentToDatabase(GeospatialDocument document, int port);

    /**
     * Performs a query with given {@code info}.
     *
     * @param info The info to use when querying.
     * @param port The port that the database is listening on.
     * @return Returns the result of the query.
     */
    QueryResult query(QueryInfo info, int port);
}
