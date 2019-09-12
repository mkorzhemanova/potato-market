package com.epam.potato.resources

import com.epam.potato.api.PotatoBag
import com.epam.potato.db.PotatoBagStore
import io.dropwizard.testing.junit.ResourceTestRule
import org.junit.ClassRule
import spock.lang.Specification

import java.time.OffsetDateTime
import java.time.ZoneOffset

class PotatoMarketResourceTest extends Specification {

    private PotatoBagStore dao
    @ClassRule
    public ResourceTestRule resources

    void setup() {
        dao = Mock(PotatoBagStore.class)
        resources = ResourceTestRule.builder()
                .addResource(new PotatoMarketResource(dao))
                .build()

    }

    def "Fetch"() {
        given:
        final potatoBag = PotatoBag.builder()
                .id('id')
                .potatoCount(44)
                .supplier("Yunnan Spices")
                .packageDateTime(OffsetDateTime.of(2019, 9, 11,
                        22, 11, 11, 11,
                        ZoneOffset.UTC))
                .price(BigDecimal.valueOf(33.1))
                .build()
        dao.fetch(*_) >> [potatoBag]

        when:
        final result = resources.target("/potato-market")
                .request()
                .get(List.class)

        then:
        assert result
        assert result.first() == potatoBag
    }

    def "Add"() {
    }
}
