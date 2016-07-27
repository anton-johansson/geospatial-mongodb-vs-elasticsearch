package com.antonjohansson.geotest;

import com.antonjohansson.geotest.docker.DockerContainer;
import com.antonjohansson.geotest.docker.DockerManager;

/**
 * Contains the applications main entry-point.
 */
public class EntryPoint
{
    /**
     * The applications main entry-point.
     */
    public static void main(String[] args)
    {
        // TODO[anton] temporary
        try (DockerManager manager = new DockerManager())
        {
            try (DockerContainer container = manager.getContainer("elasticsearch"))
            {
                System.out.println(container.getContainerId());
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
