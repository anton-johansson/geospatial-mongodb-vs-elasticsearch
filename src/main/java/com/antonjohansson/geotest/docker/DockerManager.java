package com.antonjohansson.geotest.docker;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;

/**
 * Manages docker containers.
 */
public class DockerManager implements AutoCloseable
{
    private final DefaultDockerClient client;

    /**
     * Constructs a new {@link DockerManager} using the system environment variables.
     */
    public DockerManager()
    {
        try
        {
            client = DefaultDockerClient.fromEnv().build();
        }
        catch (DockerCertificateException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a new container with given {@code name} and {@code config}.
     *
     * @param name The name of the container.
     * @param config The configuration for the container.
     */
    public DockerContainer getContainer(String name, ContainerConfig config)
    {
        try
        {
            ContainerCreation creationInfo = client.createContainer(config, name);
            String containerId = creationInfo.id();
            client.startContainer(containerId);
            return new DockerContainer(client, containerId);
        }
        catch (DockerException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close()
    {
        client.close();
    }
}
