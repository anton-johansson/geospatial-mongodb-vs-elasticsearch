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

    @Option(name = "--port", aliases = "-p", metaVar = "<port>", usage = "The port to run the database on. Useful if the default is busy.")
    private int port = PORT_DEFAULT;

    @Option(name = "--test-name", aliases = "-t", metaVar = "<name>", required = true, usage = "The name of the test to execute.")
    private String testName;

    @Option(name = "--help", help = true, usage = "Displays this help.")
    private Boolean help;

    @Option(name = "--seed", aliases = "-s", metaVar = "<seed>", usage = "The seed for the random generator. If absent, a seed based on the "
        + "current time will be used. A constant seed is useful for consistency over multiple tests.")
    private Long seed;

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
}
