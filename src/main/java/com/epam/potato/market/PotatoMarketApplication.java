package com.epam.potato.market;

import com.epam.potato.market.db.PotatoBagDao;
import com.epam.potato.market.health.NitriteDatabaseHealthCheck;
import com.epam.potato.market.resources.PotatoBagResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.dizitart.no2.Nitrite;

public class PotatoMarketApplication extends Application<PotatoMarketConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PotatoMarketApplication().run(args);
    }

    @Override
    public String getName() {
        return "potato-market";
    }

    @Override
    public void initialize(final Bootstrap<PotatoMarketConfiguration> bootstrap) {
        setupObjectMapper(bootstrap.getObjectMapper());
    }

    @Override
    public void run(final PotatoMarketConfiguration configuration, final Environment environment) {
        final Nitrite database = initNitriteDb();
        final PotatoBagDao potatoBagDao = initPotatoBagStore(database);

        environment.jersey().register(new PotatoBagResource(potatoBagDao));

        environment.healthChecks().register("nitrite", new NitriteDatabaseHealthCheck(database));
    }

    private static void setupObjectMapper(ObjectMapper objectMapper) {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private Nitrite initNitriteDb() {
        return Nitrite.builder()
                .openOrCreate();
    }

    private PotatoBagDao initPotatoBagStore(Nitrite database) {
        return new PotatoBagDao(database);
    }
}
