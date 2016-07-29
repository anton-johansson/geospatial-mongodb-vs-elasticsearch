package com.antonjohansson.geotest;

import static java.lang.Boolean.TRUE;

import java.util.Optional;

import org.kohsuke.args4j.Option;

/**
 * Contains application arguments.
 */
public class ApplicationArguments
{
    private static final int PORT_DEFAULT = 666;
    private static final int DOCUMENT_COUNT_DEFAULT = 1000;
    private static final int QUERY_COUNT_DEFAULT = 500;

    @Option(name = "--port", aliases = "-p", metaVar = "<port>", usage = "The port to run the database on. Useful if the default is busy.")
    private int port = PORT_DEFAULT;

    @Option(name = "--test-name", aliases = "-t", metaVar = "<name>", required = true, usage = "The name of the test to execute.")
    private String testName;

    @Option(name = "--help", help = true, usage = "Displays this help.")
    private Boolean help;

    @Option(name = "--seed", aliases = "-s", metaVar = "<seed>", usage = "The seed for the random generator. If absent, a seed based on the "
        + "current time will be used. A constant seed is useful for consistency over multiple tests.")
    private Long seed;

    @Option(name = "--document-count", aliases = "-d", metaVar = "<document count>", usage = "Specifies the number of documents that will be "
        + "added to the database. Higher value give higher precision results.")
    private int documentCount = DOCUMENT_COUNT_DEFAULT;

    @Option(name = "--query-count", aliases = "-q", metaVar = "<query count>", usage = "Specifies the number of queries that will be executed. "
        + "Higher value give higher precision results.")
    private int queryCount = QUERY_COUNT_DEFAULT;

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getTestName()
    {
        return testName;
    }

    public void setTestName(String testName)
    {
        this.testName = testName;
    }

    public boolean isHelp()
    {
        return TRUE.equals(help);
    }

    public void setHelp(boolean help)
    {
        this.help = help;
    }

    public Optional<Long> getSeed()
    {
        return Optional.ofNullable(seed);
    }

    public void setSeed(long seed)
    {
        this.seed = seed;
    }

    public int getDocumentCount()
    {
        return documentCount;
    }

    public void setDocumentCount(int documentCount)
    {
        this.documentCount = documentCount;
    }

    public int getQueryCount()
    {
        return queryCount;
    }

    public void setQueryCount(int queryCount)
    {
        this.queryCount = queryCount;
    }
}
