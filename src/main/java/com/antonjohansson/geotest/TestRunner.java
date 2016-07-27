package com.antonjohansson.geotest;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.Collection;

import com.antonjohansson.geotest.docker.DockerContainer;
import com.antonjohansson.geotest.docker.DockerManager;
import com.antonjohansson.geotest.model.GeospatialDocument;
import com.antonjohansson.geotest.model.TestDetails;
import com.antonjohansson.geotest.test.framework.ITestable;
import com.antonjohansson.geotest.utils.RandomGenerator;
import com.google.common.collect.ImmutableMap;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;

/**
 * Runs tests.
 */
public class TestRunner
{
    private static final int DOCUMENT_COUNT = 1000;

    private final RandomGenerator random;
    private final DockerManager manager;
    private final ITestable tester;

    public TestRunner(RandomGenerator random, DockerManager manager, ITestable tester)
    {
        this.random = random;
        this.manager = manager;
        this.tester = tester;
    }

    /**
     * Runs the test.
     *
     * @param port The port to communicate with the database on.
     * @return Returns the details of the test.
     */
    public TestDetails run(int port)
    {
        PortBinding from = PortBinding.of("", valueOf(port));
        String to = tester.getDockerExposedPort() + "/tcp";

        HostConfig hostConfig = HostConfig.builder()
                .portBindings(ImmutableMap.of(to, asList(from)))
                .build();

        String image = tester.getDockerImageName();
        ContainerConfig config = ContainerConfig.builder()
                .image(image)
                .hostConfig(hostConfig)
                .build();

        try (DockerContainer container = manager.getContainer(image, config))
        {
            Collection<GeospatialDocument> documents = generateDocuments();
            tester.addDocumentsToDatabase(documents, port);

            return new TestDetails();
        }
    }

    private Collection<GeospatialDocument> generateDocuments()
    {
        return range(0, DOCUMENT_COUNT)
                .boxed()
                .map(value -> generateDocument())
                .collect(toList());
    }

    private GeospatialDocument generateDocument()
    {
        GeospatialDocument document = new GeospatialDocument();
        document.setLatitude(random.getLatitude());
        document.setLongitude(random.getLongitude());
        document.setCreationDate(random.getCreationDate());
        document.setValue(random.getValue());
        return document;
    }
}
