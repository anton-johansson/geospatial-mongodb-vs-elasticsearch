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
     * Gets a new container with given {@code image}.
     */
    public DockerContainer getContainer(String image)
    {
        ContainerConfig config = ContainerConfig.builder()
                .image(image)
                .build();

        try
        {
            ContainerCreation creationInfo = client.createContainer(config, image);
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
    public void close() throws Exception
    {
        client.close();
    }
}
