package com.epam.potato.market.resources

import com.epam.potato.market.api.PotatoBag
import com.epam.potato.market.db.PotatoBagDao
import io.dropwizard.testing.junit.ResourceTestRule
import org.junit.Rule
import spock.lang.Specification

import javax.ws.rs.client.Entity
import javax.ws.rs.core.GenericType
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import java.time.OffsetDateTime
import java.time.ZoneOffset

class PotatoBagResourceTest extends Specification {
    private static final String BASE_ENDPOINT = '/potato-market/potato-bags'
    private static final int DEFAULT_FETCH_SIZE = 3

    private PotatoBagDao dao = Mock(PotatoBagDao)

    @Rule
    private ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new PotatoBagResource(dao))
            .build()

    def 'GET /potato-market/potato-bags should fetch default number of PotatoBags when size is not provided'() {
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

        and:
        dao.fetch(DEFAULT_FETCH_SIZE) >> [potatoBag] * DEFAULT_FETCH_SIZE

        when:
        final result = resources.target(BASE_ENDPOINT)
                .request()
                .get(new GenericType<List<PotatoBag>>() {})

        then:
        assert result
        assert result.size() == DEFAULT_FETCH_SIZE
        result.each {
            assert it == potatoBag
        }
    }

    def 'GET /potato-market/potato-bags should fetch specified number of PotatoBags when size is provided'() {
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

        and:
        dao.fetch(size) >> [potatoBag] * size

        when:
        final result = resources.target(BASE_ENDPOINT)
                .queryParam('size', size)
                .request()
                .get(new GenericType<List<PotatoBag>>() {})

        then:
        assert result
        assert result.size() == size
        result.each {
            assert it == potatoBag
        }

        where:
        size << [1, 10, 11, 100, 101, 1000]
    }

    def 'GET /potato-market/potato-bags should return BAD_REQUEST status when invalid size is provided'() {
        when:
        final response = resources.target(BASE_ENDPOINT)
                .queryParam('size', size)
                .request()
                .get()

        then:
        assert response
        assert response.statusInfo == Response.Status.BAD_REQUEST

        where:
        size << [0, -1, -11, -100]
    }

    def 'POST /potato-market/potato-bags should return CREATED status when PotatoBag added successfully'() {
        given:
        final potatoBag = PotatoBag.builder()
                .id('id33')
                .potatoCount(44)
                .supplier('Yunnan Spices')
                .packageDateTime(OffsetDateTime.of(2019, 9, 11,
                        22, 11, 11, 11,
                        ZoneOffset.UTC))
                .price(BigDecimal.valueOf(33.1))
                .build()

        and:
        dao.add(potatoBag) >> 1

        when:
        final response = resources.target(BASE_ENDPOINT)
                .request()
                .post(Entity.entity(potatoBag, MediaType.APPLICATION_JSON_TYPE))

        then:
        assert response
        assert response.statusInfo == Response.Status.CREATED
        assert response.location.path == "$BASE_ENDPOINT/$potatoBag.id"
    }

    def 'POST /potato-market/potato-bags should return INTERNAL_SERVER_ERROR status when PotatoBag not added successfully'() {
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

        and:
        dao.add(potatoBag) >> 0

        when:
        final response = resources.target(BASE_ENDPOINT)
                .request()
                .post(Entity.entity(potatoBag, MediaType.APPLICATION_JSON_TYPE))

        then:
        assert response
        assert response.statusInfo == Response.Status.INTERNAL_SERVER_ERROR
    }
}
