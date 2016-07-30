package com.antonjohansson.geotest.docker;

import static com.spotify.docker.client.DockerClient.RemoveContainerParam.removeVolumes;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerInfo;

/**
 * Defines a single docker container.
 */
public class DockerContainer implements AutoCloseable
{
    private final DockerClient client;
    private final String containerId;

    /**
     * Constructs a new {@link DockerContainer}.
     *
     * @param client The docker client. Used to close the container.
     * @param containerId The identifier of the container.
     */
    DockerContainer(DockerClient client, String containerId)
    {
        this.client = client;
        this.containerId = containerId;
    }

    public String getContainerId()
    {
        return containerId;
    }

    @Override
    public void close()
    {
        try
        {
            ContainerInfo info = client.inspectContainer(containerId);
            if (info.state().running())
            {
                client.killContainer(containerId);
            }
            client.removeContainer(containerId, removeVolumes());
        }
        catch (DockerException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
