package com.sk.test.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindPolicyRequestDTO {

    @ApiModelProperty(notes = "Request date", example = "2022-11-24", required = true)
    private LocalDate requestDate;

    @ApiModelProperty(notes = "Policy id", example = "UUA123456789", required = true)
    private String policyId;

}

