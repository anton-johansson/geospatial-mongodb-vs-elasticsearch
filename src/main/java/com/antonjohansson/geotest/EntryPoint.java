package com.antonjohansson.geotest;

import static java.util.Arrays.asList;
import static org.kohsuke.args4j.OptionHandlerFilter.REQUIRED;

import java.io.PrintStream;
import java.util.Collection;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.antonjohansson.geotest.docker.DockerManager;
import com.antonjohansson.geotest.model.TestDetails;
import com.antonjohansson.geotest.test.elasticsearch.Elasticsearch;
import com.antonjohansson.geotest.test.framework.ITestable;
import com.antonjohansson.geotest.utils.RandomGenerator;

/**
 * Contains the applications main entry-point.
 */
public class EntryPoint
{
    private static final String EXEC = "java geospatial-mongodb-vs-elasticsearch.jar";
    private static final Collection<? extends ITestable> TESTABLES = asList(new Elasticsearch());

    private final ApplicationArguments arguments;
    private final CmdLineParser parser;

    private EntryPoint()
    {
        arguments = new ApplicationArguments();
        parser = new CmdLineParser(arguments);
    }

    /**
     * The applications main entry-point.
     */
    public static void main(String[] args)
    {
        EntryPoint entryPoint = new EntryPoint();
        entryPoint.parseArguments(args);
        entryPoint.run();
    }

    private void parseArguments(String[] args)
    {
        try
        {
            parser.parseArgument(args);
        }
        catch (CmdLineException e)
        {
            error(e.getMessage());
        }
    }

    private void error(String message)
    {
        System.err.println(message);
        System.err.println();
        help(System.err);
        System.exit(1);
    }

    private void run()
    {
        if (arguments.isHelp())
        {
            help(System.out);
            return;
        }

        ITestable testable = getTestable(arguments.getTestName());
        RandomGenerator random = getRandom(arguments);

        try (DockerManager manager = getDockerManager())
        {
            System.out.println("Running '" + testable.getLabel() + "'.");

            TestRunner runner = new TestRunner(random, manager, testable);
            TestDetails details = runner.run(arguments.getPort());

            System.out.println("Details:");
            System.out.println(details);
        }
    }

    private void help(PrintStream stream)
    {
        stream.println(EXEC + " [options...]");
        parser.printUsage(stream);
        stream.println();
        stream.println("Example:");
        stream.println(EXEC + parser.printExample(REQUIRED));
    }

    private ITestable getTestable(String name)
    {
        for (ITestable testable : TESTABLES)
        {
            if (testable.getLabel().equalsIgnoreCase(name))
            {
                return testable;
            }
        }

        error("Invalid \"--test-name\": " + name);
        return null;
    }

    private RandomGenerator getRandom(ApplicationArguments arguments)
    {
        return arguments.getSeed()
                .map(RandomGenerator::new)
                .orElseGet(RandomGenerator::new);
    }

    private DockerManager getDockerManager()
    {
        return new DockerManager();
    }
}
