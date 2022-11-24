package com.sk.test.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDocument {
    private Integer id;
    @Nonnull
    private String firstName;
    @Nonnull
    private String secondName;
    @Nonnull
    private BigDecimal premium;
}
