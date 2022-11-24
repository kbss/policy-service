package com.sk.test.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InsuredPersonDTO {

    @ApiModelProperty(notes = "Person id in policy")
    private Integer id;

    @ToString.Exclude
    @ApiModelProperty(notes = "Person First Name", example = "John", required = true)
    private String firstName;

    @ToString.Exclude
    @ApiModelProperty(notes = "Person Second Name", example = "Doe", required = true)
    private String secondName;

    @ApiModelProperty(notes = "Premium value", example = "10.0", required = true)
    private BigDecimal premium;

}

