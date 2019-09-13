package com.epam.potato.market.api

import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import java.time.OffsetDateTime

class PotatoBagValidationTest extends Specification {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator()

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
        final validationResults = VALIDATOR.validate(bag)

        then:
        assert validationResults.isEmpty()

        where:
        supplier << ['De Coster', 'Owel', 'Patatas Ruben', 'Yunnan Spices']
    }

    def 'validator should return constraint violation when id is null'() {
        given:
        final PotatoBag bag = PotatoBag.builder()
                .id(null)
                .potatoCount(11)
                .supplier('Patatas Ruben')
                .packageDateTime(OffsetDateTime.now().minusHours(1))
                .price(BigDecimal.ONE)
                .build()

        when:
        final validationResults = VALIDATOR.validate(bag)

        then:
        assertConstraintViolationMessageForProperty(validationResults, 'id', 'may not be empty')

        where:
        id << [null, '', '   ']
    }

    def 'validator should return constraint violation when potatoCount is null'() {
        given:
        final PotatoBag bag = PotatoBag.builder()
                .id('id')
                .potatoCount(null)
                .supplier('Patatas Ruben')
                .packageDateTime(OffsetDateTime.now().minusHours(1))
                .price(BigDecimal.ONE)
                .build()

        when:
        final validationResults = VALIDATOR.validate(bag)

        then:
        assertNotNullConstraintViolationForProperty(validationResults, 'potatoCount')
    }

    def 'validator should return constraint violation when potatoCount is not between 1 and 100'() {
        given:
        final bag = PotatoBag.builder()
                .id('id')
                .potatoCount(potatoCount)
                .supplier('Patatas Ruben')
                .packageDateTime(OffsetDateTime.now().minusHours(1))
                .price(BigDecimal.ONE)
                .build()

        when:
        final validationResults = VALIDATOR.validate(bag)

        then:
        assertConstraintViolationMessageForProperty(validationResults, 'potatoCount', 'must be between 1 and 100')

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
        final validationResults = VALIDATOR.validate(bag)

        then:
        assertNotNullConstraintViolationForProperty(validationResults, 'supplier')
    }

    def 'validator should return constraint violation when packageDateTime is null'() {
        given:
        final bag = PotatoBag.builder()
                .id('id')
                .potatoCount(11)
                .supplier('Patatas Ruben')
                .packageDateTime(null)
                .price(BigDecimal.ONE)
                .build()

        when:
        final validationResults = VALIDATOR.validate(bag)

        then:
        assertNotNullConstraintViolationForProperty(validationResults, 'packageDateTime')
    }

    def 'validator should return constraint violation when packageDateTime is not in the past'() {
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
        final validationResults = VALIDATOR.validate(bag)

        then:
        assertConstraintViolationMessageForProperty(validationResults, 'packageDateTime', 'must be in the past')
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
        final validationResults = VALIDATOR.validate(bag)

        then:
        assertNotNullConstraintViolationForProperty(validationResults, 'price')
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
        final validationResults = VALIDATOR.validate(bag)

        then:
        assertConstraintViolationMessageForProperty(validationResults, 'price', 'must be between 0 and 50')

        where:
        price << [-51, -1, -0.1, 50.1, 51]
    }

    private static void assertNotNullConstraintViolationForProperty(Set<ConstraintViolation<PotatoBag>> constraintViolations, String propertyName) {
        assertConstraintViolationMessageForProperty(constraintViolations, propertyName, 'may not be null')
    }

    private static void assertConstraintViolationMessageForProperty(Set<ConstraintViolation<PotatoBag>> constraintViolations, String propertyName, String message) {
        assert constraintViolations.find { constraintViolation ->
            constraintViolation.propertyPath.first().name == propertyName &&
                    constraintViolation.message == message
        }
    }
}
