package com.epam.potato.market

import com.epam.potato.market.api.PotatoBag
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.testing.DropwizardTestSupport
import io.dropwizard.testing.ResourceHelpers
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.lang.Stepwise

import javax.ws.rs.client.Client
import javax.ws.rs.client.Entity
import javax.ws.rs.core.GenericType
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Stepwise
class PotatoMarketApplicationTest extends Specification {

    private static final String BASE_ENDPOINT = '/potato-market/potato-bags'

    @AutoCleanup('after')
    private static final DropwizardTestSupport<PotatoMarketTestConfiguration> SUPPORT =
            new DropwizardTestSupport<PotatoMarketTestConfiguration>(
                    PotatoMarketApplication,
                    ResourceHelpers.resourceFilePath('test-config.yml'))

    @AutoCleanup
    private static Client client
    private static String applicationUri

    void setupSpec() {
        SUPPORT.before()
        client = new JerseyClientBuilder(SUPPORT.getEnvironment()).build('Test Client')
        applicationUri = String.format('http://localhost:%d%s', SUPPORT.getLocalPort(), BASE_ENDPOINT)
    }

    def 'POST /potato-market/potato-bags should return CREATED status when PotatoBag added successfully'() {
        given:
        final potatoBag = PotatoBag.builder()
                .id('id')
                .potatoCount(44)
                .supplier('Yunnan Spices')
                .packageDateTime(OffsetDateTime.of(2019, 9, 11,
                        22, 11, 11, 11,
                        ZoneOffset.UTC))
                .price(BigDecimal.valueOf(33.1))
                .build()

        when:
        final response = client
                .target(applicationUri)
                .request()
                .post(Entity.entity(potatoBag, MediaType.APPLICATION_JSON_TYPE))

        then:
        assert response
        assert response.statusInfo == Response.Status.CREATED
        assert response.location.getPath() == BASE_ENDPOINT
    }

    def 'GET /potato-market/potato-bags should PotatoBags'() {
        when:
        final result = client
                .target(applicationUri)
                .request()
                .get(new GenericType<List<PotatoBag>>() {})

        then:
        assert result
    }
}