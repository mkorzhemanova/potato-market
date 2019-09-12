package com.epam.potato.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.OneOf;
import lombok.Builder;
import lombok.Value;
import org.dizitart.no2.objects.Id;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Value
@Builder
public class PotatoBag {

    @Id
    @NotBlank
    @JsonProperty
    private final String id;

    @NotNull
    @Range(min = 1, max = 100)
    @JsonProperty
    private final Integer potatoCount;

    @NotNull
    @OneOf({"De Coster", "Owel", "Patatas Ruben", "Yunnan Spices"})
    @JsonProperty
    private final String supplier;

    @NotNull
    @Past
    @JsonProperty
    private final OffsetDateTime packageDateTime;

    @NotNull
    @Range(max = 50)
    @JsonProperty
    private final BigDecimal price;
}
