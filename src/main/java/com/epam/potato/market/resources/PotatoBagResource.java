package com.epam.potato.market.resources;


import com.epam.potato.market.api.PotatoBag;
import com.epam.potato.market.db.PotatoBagDao;
import io.dropwizard.jersey.params.IntParam;
import lombok.RequiredArgsConstructor;
import org.dizitart.no2.exceptions.UniqueConstraintException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

@Path("/potato-market/potato-bags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class PotatoBagResource {

    private final PotatoBagDao potatoBagDao;

    @GET
    public List<PotatoBag> fetch(@QueryParam("size") @DefaultValue("3") @Min(1) IntParam size) {
        final List<PotatoBag> result = potatoBagDao.fetch(size.get());
        if (result != null && !result.isEmpty()) {
            return result;
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @POST
    public Response add(@NotNull @Valid PotatoBag potatoBag) {
        try {
            final int insertResultCount = potatoBagDao.add(potatoBag);
            if (insertResultCount > 0) {
                return Response
                        .created(UriBuilder.fromResource(PotatoBagResource.class)
                                .path("{id}")
                                .build(potatoBag.getId(), "id"))
                        .build();
            }
        } catch (UniqueConstraintException e) {
            throw new WebApplicationException(String.format("Potato Bag (id='%s') already exists", potatoBag.getId()),
                    Response.Status.CONFLICT);
        }
        throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }
}
