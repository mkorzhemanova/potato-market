package com.epam.potato.api

import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import java.time.OffsetDateTime

class PotatoBagValidationTest extends Specification {

    private static Validator validator

    def setup() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()
    }

    def 'validator should not return constraint violations when PotatoBag is valid'() {
        given:
        final random = new Random()
        final int randomPotatoCount = random.ints(1, 1, 101).findFirst().asInt
        final double randomPrice = random.doubles(1, 0, 50).findFirst().asDouble
        final bag = PotatoBag.builder()
                .id('id')
                .potatoCount(randomPotatoCount)
                .supplier(supplier)
                .packageDateTime(OffsetDateTime.now().minusHours(1))
                .price(BigDecimal.valueOf(randomPrice))
                .build()

        when:
        final Set<ConstraintViolation<PotatoBag>> validationResults = validator.validate(bag)

        then:
        assert validationResults.isEmpty()

        where:
        supplier << ['De Coster', 'Owel', 'Patatas Ruben', 'Yunnan Spices']
    }

    def 'validator should return constraint violation when number of potatoes is null'() {
        given:
        final PotatoBag bag = PotatoBag.builder()
                .id('id')
                .potatoCount(null)
                .supplier('Patatas Ruben')
                .packageDateTime(OffsetDateTime.now().minusHours(1))
                .price(BigDecimal.ONE)
                .build()

        when:
        final Set<ConstraintViolation<PotatoBag>> validationResults = validator.validate(bag)

        then:
        assert !validationResults.isEmpty()
        assertNotNullConstraintForProperty(validationResults.first(), 'potatoCount')
    }

    def 'validator should return constraint violation when number of potatoes is not between 1 and 100'() {
        given:
        final bag = PotatoBag.builder()
                .id('id')
                .potatoCount(potatoCount)
                .supplier('Patatas Ruben')
                .packageDateTime(OffsetDateTime.now().minusHours(1))
                .price(BigDecimal.ONE)
                .build()

        when:
        final Set<ConstraintViolation<PotatoBag>> validationResults = validator.validate(bag)

        then:
        assert !validationResults.isEmpty()
        assert validationResults.first().message == 'must be between 1 and 100'

        where:
        potatoCount << [0, -1, -101, 101, 1000]
    }

    def 'validator should return constraint violation when supplier is null'() {
        given:
        final bag = PotatoBag.builder()
                .id('id')
                .potatoCount(11)
                .supplier(null)
                .packageDateTime(OffsetDateTime.now().minusHours(1))
                .price(BigDecimal.ONE)
                .build()

        when:
        final Set<ConstraintViolation<PotatoBag>> validationResults = validator.validate(bag)

        then:
        assert !validationResults.isEmpty()
        assertNotNullConstraintForProperty(validationResults.first(), 'supplier')
    }

    def 'validator should return constraint violation when package dateTime is null'() {
        given:
        final bag = PotatoBag.builder()
                .id('id')
                .potatoCount(11)
                .supplier('Patatas Ruben')
                .packageDateTime(null)
                .price(BigDecimal.ONE)
                .build()

        when:
        final Set<ConstraintViolation<PotatoBag>> validationResults = validator.validate(bag)

        then:
        assert !validationResults.isEmpty()
        assertNotNullConstraintForProperty(validationResults.first(), 'packageDateTime')
    }

    def 'validator should return constraint violation when package dateTime is not in the past'() {
        given:
        final packageDateTime = OffsetDateTime.now().plusDays(1)
        final bag = PotatoBag.builder()
                .id('id')
                .potatoCount(11)
                .supplier('Patatas Ruben')
                .packageDateTime(packageDateTime)
                .price(BigDecimal.ONE)
                .build()

        when:
        final Set<ConstraintViolation<PotatoBag>> validationResults = validator.validate(bag)

        then:
        assert !validationResults.isEmpty()
        assert validationResults.first().message == "must be in the past"
    }

    def 'validator should return constraint violation when price is null'() {
        given:
        final bag = PotatoBag.builder()
                .id('id')
                .potatoCount(11)
                .supplier('Patatas Ruben')
                .packageDateTime(OffsetDateTime.now().minusHours(1))
                .price(null)
                .build()

        when:
        final Set<ConstraintViolation<PotatoBag>> validationResults = validator.validate(bag)

        then:
        assert !validationResults.isEmpty()
        assertNotNullConstraintForProperty(validationResults.first(), 'price')
    }

    def 'validator should return constraint violation when price is not between 0 and 50'() {
        given:
        final bag = PotatoBag.builder()
                .id('id')
                .potatoCount(11)
                .supplier('Patatas Ruben')
                .packageDateTime(OffsetDateTime.now().minusHours(1))
                .price(BigDecimal.valueOf(price as double))
                .build()

        when:
        final Set<ConstraintViolation<PotatoBag>> validationResults = validator.validate(bag)

        then:
        assert !validationResults.isEmpty()
        assert validationResults.first().message == 'must be between 0 and 50'

        where:
        price << [-51, -1, -0.1, 50.1, 51]
    }

    private static void assertNotNullConstraintForProperty(ConstraintViolation<PotatoBag> constraintViolation, String propertyName) {
        assert constraintViolation.message == 'may not be null'
        assert constraintViolation.propertyPath.first().name == propertyName
    }
}
