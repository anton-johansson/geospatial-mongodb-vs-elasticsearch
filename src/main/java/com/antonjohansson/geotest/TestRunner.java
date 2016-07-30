package com.antonjohansson.geotest;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.apache.commons.lang.ArrayUtils.toPrimitive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.LongStream;

import com.antonjohansson.geotest.docker.DockerContainer;
import com.antonjohansson.geotest.docker.DockerManager;
import com.antonjohansson.geotest.model.GeospatialDocument;
import com.antonjohansson.geotest.model.QueryInfo;
import com.antonjohansson.geotest.model.QueryResult;
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
    private static final int SLEEP_TIME = 10000;

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
    public TestDetails run(int port, int documentCount, int queryCount)
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
            System.out.println("Waiting for database to start...");
            Thread.sleep(SLEEP_TIME);

            System.out.println("Preparing the database...");
            tester.prepare(port);

            Collection<Long> addExecutionTimes = new ArrayList<>();
            Collection<Long> queryExecutionTimes = new ArrayList<>();

            Collection<GeospatialDocument> documents = generateDocuments(documentCount);
            Collection<QueryInfo> queryInfos = generateQueryInfos(queryCount);

            // Add documents to the database
            System.out.println("Inserting documents...");
            for (GeospatialDocument document : documents)
            {
                long start = System.currentTimeMillis();
                tester.addDocumentToDatabase(document, port);
                long time = System.currentTimeMillis() - start;
                addExecutionTimes.add(time);
            }

            // Query the database
            System.out.println("Querying...");
            List<QueryResult> results = new ArrayList<>();
            for (QueryInfo info : queryInfos)
            {
                long start = System.currentTimeMillis();
                QueryResult result = tester.query(info, port);
                result.setQueryId(info.getQueryId());
                result.setLongitude(info.getLongitude());
                result.setLatitude(info.getLatitude());
                results.add(result);
                long time = System.currentTimeMillis() - start;
                queryExecutionTimes.add(time);
            }

            System.out.println("Done!");

            long averageAddTime = (long) LongStream.of(toArray(addExecutionTimes)).average().orElseThrow(() -> new IllegalStateException("expected values"));
            long maxAddTime = LongStream.of(toArray(addExecutionTimes)).max().orElseThrow(() -> new IllegalStateException("expected values"));
            long minAddTime = LongStream.of(toArray(addExecutionTimes)).min().orElseThrow(() -> new IllegalStateException("expected values"));
            long averageQueryTime = (long) LongStream.of(toArray(queryExecutionTimes)).average().orElseThrow(() -> new IllegalStateException("expected values"));
            long maxQueryTime = LongStream.of(toArray(queryExecutionTimes)).max().orElseThrow(() -> new IllegalStateException("expected values"));
            long minQueryTime = LongStream.of(toArray(queryExecutionTimes)).min().orElseThrow(() -> new IllegalStateException("expected values"));

            TestDetails testDetails = new TestDetails();
            testDetails.setAverageAddTime(averageAddTime);
            testDetails.setMaxAddTime(maxAddTime);
            testDetails.setMinAddTime(minAddTime);
            testDetails.setAverageQueryTime(averageQueryTime);
            testDetails.setMaxQueryTime(maxQueryTime);
            testDetails.setMinQueryTime(minQueryTime);
            testDetails.setResults(results);
            return testDetails;
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private long[] toArray(Collection<Long> collection)
    {
        Long[] array = collection.toArray(new Long[collection.size()]);
        return toPrimitive(array);
    }

    private Collection<GeospatialDocument> generateDocuments(int documentCount)
    {
        return range(0, documentCount)
                .boxed()
                .map(this::generateDocument)
                .collect(toList());
    }

    private Collection<QueryInfo> generateQueryInfos(int queryCount)
    {
        return range(0, queryCount)
                .boxed()
                .map(this::generateQueryInfo)
                .collect(toList());
    }

    private GeospatialDocument generateDocument(int index)
    {
        GeospatialDocument document = new GeospatialDocument();
        document.setId(index + 1);
        document.setLatitude(random.getLatitude());
        document.setLongitude(random.getLongitude());
        document.setCreationDate(random.getCreationDate());
        document.setValue(random.getValue());
        return document;
    }

    private QueryInfo generateQueryInfo(int index)
    {
        QueryInfo info = new QueryInfo();
        info.setQueryId(index + 1);
        info.setLatitude(random.getLatitude());
        info.setLongitude(random.getLongitude());
        return info;
    }
}
