package com.sk.test.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BasePolicyDTO {
    @ApiModelProperty(notes = "Policy ID", example = "UUA123456789")
    private String policyId;

    @ApiModelProperty(notes = "Insured persons list")
    private List<InsuredPersonDTO> insuredPersons;

    @ApiModelProperty(notes = "Total premium")
    private BigDecimal totalPremium;
}
