package com.epam.potato;

import com.epam.potato.db.PotatoBagStore;
import com.epam.potato.resources.PotatoMarketResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.dizitart.no2.Nitrite;

public class PotatoApplication extends Application<PotatoConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PotatoApplication().run(args);
    }

    @Override
    public String getName() {
        return "potato";
    }

    @Override
    public void initialize(final Bootstrap<PotatoConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final PotatoConfiguration configuration, final Environment environment) {
        final Nitrite db = Nitrite.builder()
                .openOrCreate();
        final PotatoBagStore dao = new PotatoBagStore(db);
        environment.jersey().register(new PotatoMarketResource(dao));
    }

}
