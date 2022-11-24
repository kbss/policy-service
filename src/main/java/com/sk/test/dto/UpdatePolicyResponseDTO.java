package com.sk.test.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdatePolicyResponseDTO extends BasePolicyDTO {

    @ApiModelProperty(notes = "Effective start date", example = "2022-11-24", required = true)
    private LocalDate effectiveDate;

}

