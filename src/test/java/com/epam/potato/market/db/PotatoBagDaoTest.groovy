package com.epam.potato.market.db


import com.epam.potato.market.api.PotatoBag
import org.dizitart.no2.Nitrite
import org.dizitart.no2.exceptions.UniqueConstraintException
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Subject

import java.time.OffsetDateTime
import java.time.ZoneOffset

@Stepwise
class PotatoBagDaoTest extends Specification {

    @AutoCleanup
    private Nitrite database

    @Subject
    private PotatoBagDao sut

    def setup() {
        database = Nitrite.builder().openOrCreate()
        sut = new PotatoBagDao(database)
    }

    def 'should insert PotatoBag into the database'() {
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
        final insertResultCount = sut.add(potatoBag)

        then:
        assert insertResultCount == 1
    }

    def 'should throw UniqueConstraintException when PotatoBag already exists'() {
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
        sut.add(potatoBag)
        sut.add(potatoBag)

        then:
        thrown(UniqueConstraintException)
    }

    def 'should fetch PotatoBags from the database'() {
        given:
        addTestPotatoBags(databaseSize)

        when:
        final potatoBags = sut.fetch(size)

        then:
        assert potatoBags.size() == Math.min(size, databaseSize)

        where:
        size | databaseSize
        10   | 0
        10   | 1
        10   | 2
        10   | 3
        1    | 10
        2    | 10
        3    | 10
        1    | 1
        2    | 2
        10   | 10
    }

    private void addTestPotatoBags(int count) {
        for (i in (0..<count)) {
            final potatoBag = PotatoBag.builder()
                    .id("id_$i")
                    .potatoCount(44)
                    .supplier('Yunnan Spices')
                    .packageDateTime(OffsetDateTime.of(2019, 9, 11,
                            22, 11, 11, 11,
                            ZoneOffset.UTC))
                    .price(BigDecimal.valueOf(33.1))
                    .build()
            sut.add(potatoBag)
        }
    }
}
