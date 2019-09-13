package com.epam.potato.market.api


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.dropwizard.jackson.Jackson
import spock.lang.Specification

import java.time.OffsetDateTime
import java.time.ZoneOffset

import static io.dropwizard.testing.FixtureHelpers.fixture

class PotatoBagSerializationTest extends Specification {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    def 'should serialize PotatoBag to JSON'() {
        given:
        final potatoBag = PotatoBag.builder()
                .id('id')
                .potatoCount(44)
                .supplier('Yunnan Spices')
                .packageDateTime(OffsetDateTime.of(2019, 9, 11,
                        22, 11, 11, 11,
                        ZoneOffset.of("+3")))
                .price(BigDecimal.valueOf(33.1))
                .build()

        and:
        final expected = fixture('fixtures/potatoBag.json')

        when:
        final String result = MAPPER.writeValueAsString(potatoBag)

        then:
        assert result
        assert result == expected
    }

    def 'should deserialize PotatoBag from JSON'() {
        given:
        final expected = PotatoBag.builder()
                .id('id')
                .potatoCount(44)
                .supplier('Yunnan Spices')
                .packageDateTime(OffsetDateTime.of(2019, 9, 11,
                        22, 11, 11, 11,
                        ZoneOffset.UTC))
                .price(BigDecimal.valueOf(33.1))
                .build()

        when:
        final result = MAPPER.readValue(fixture('fixtures/potatoBag.json'), PotatoBag)

        then:
        assert result == expected
    }
}
