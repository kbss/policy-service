package com.sk.test.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreatePolicyRequestDTO {

    @ApiModelProperty(notes = "Policy start date", example = "2022-11-24", required = true)
    @NotNull(message = "startDate is mandatory")
    private LocalDate startDate;

    @Size(min = 1, message = "Insured persons required")
    @ApiModelProperty(notes = "Insured persons list", required = true)
    private List<InsuredPersonDTO> insuredPersons;

}

