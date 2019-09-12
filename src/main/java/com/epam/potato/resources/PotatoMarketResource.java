package com.epam.potato.resources;

import com.epam.potato.api.PotatoBag;
import com.epam.potato.db.PotatoBagStore;
import io.dropwizard.jersey.params.IntParam;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

@Path("/potato-market")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class PotatoMarketResource {

    private final PotatoBagStore potatoBagStore;

    @GET
    public List<PotatoBag> fetch(@QueryParam("count") @DefaultValue("3") IntParam count) {
        final List<PotatoBag> result = potatoBagStore.fetch(0, count.get());
        if (result != null && !result.isEmpty()) {
            return result;
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @POST
    public Response add(@NotNull @Valid PotatoBag potatoBag) {
        final int count = potatoBagStore.add(potatoBag);
        if (count > 0) {
            return Response.created(UriBuilder.fromResource(PotatoMarketResource.class)
                    .build(potatoBag.getId()))
                    .build();
        }
        throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }
}
