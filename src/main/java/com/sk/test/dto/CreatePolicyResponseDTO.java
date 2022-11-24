package com.sk.test.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreatePolicyResponseDTO extends BasePolicyDTO {

    @ApiModelProperty(notes = "Effective start date", example = "2022-11-24", required = true)
    @NotNull(message = "startDate is mandatory")
    private LocalDate startDate;

}

