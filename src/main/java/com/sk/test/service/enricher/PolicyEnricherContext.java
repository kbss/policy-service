package com.sk.test.service.enricher;

import com.sk.test.domain.PolicyDocument;
import com.sk.test.dto.CreatePolicyRequestDTO;
import com.sk.test.dto.InsuredPersonDTO;
import com.sk.test.dto.UpdatePolicyRequestDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PolicyEnricherContext {

    private PolicyDocument policyDocument;

    private UpdatePolicyRequestDTO updatePolicy;

    private List<InsuredPersonDTO> persons;

    private CreatePolicyRequestDTO createPolicyDTO;

}
