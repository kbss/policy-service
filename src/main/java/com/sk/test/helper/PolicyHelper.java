package com.sk.test.helper;

import com.sk.test.domain.PersonDocument;
import com.sk.test.domain.PolicyDocument;
import com.sk.test.dto.BasePolicyDTO;
import com.sk.test.dto.InsuredPersonDTO;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PolicyHelper {

    public static List<InsuredPersonDTO> toDto(List<PersonDocument> source) {
        return source.stream().map(p -> InsuredPersonDTO.builder()
                        .id(p.getId())
                        .firstName(p.getFirstName())
                        .secondName(p.getSecondName())
                        .premium(p.getPremium())
                        .build())
                .toList();
    }

    public static BasePolicyDTO update(BasePolicyDTO target, PolicyDocument source) {
        target.setPolicyId(source.getPolicyId());
        target.setInsuredPersons(toDto(source.getInsuredPersons()));
        target.setTotalPremium(source.getTotalPremium());
        return target;
    }
}
