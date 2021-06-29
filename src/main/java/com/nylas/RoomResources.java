package com.nylas;

import java.io.IOException;

public class RoomResources extends RestfulDAO<RoomResource> {

    RoomResources(NylasClient client, String accessToken) {
        super(client, RoomResource.class, "resources", accessToken);
    }

    /**
     * Lists all the room resources associated with the account.
     * Note that currently this is the only operation that room resources supports
     */
    public RemoteCollection<RoomResource> list() throws IOException, RequestFailedException {
        return list(new RoomResourceQuery());
    }
}
