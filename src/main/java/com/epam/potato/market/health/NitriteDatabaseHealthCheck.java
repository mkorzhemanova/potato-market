package com.epam.potato.market.health;

import com.codahale.metrics.health.HealthCheck;
import lombok.RequiredArgsConstructor;
import org.dizitart.no2.Nitrite;

@RequiredArgsConstructor
public class NitriteDatabaseHealthCheck extends HealthCheck {

    private final Nitrite database;

    @Override
    protected Result check() {
        if (!database.isClosed()) {
            return Result.healthy();
        } else {
            return Result.unhealthy("Cannot connect to database");
        }
    }
}
