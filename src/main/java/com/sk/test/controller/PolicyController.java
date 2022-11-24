package com.sk.test.controller;


import com.sk.test.api.PolicyApi;
import com.sk.test.dto.CreatePolicyRequestDTO;
import com.sk.test.dto.CreatePolicyResponseDTO;
import com.sk.test.dto.FindPolicyRequestDTO;
import com.sk.test.dto.FindPolicyResponseDTO;
import com.sk.test.dto.UpdatePolicyRequestDTO;
import com.sk.test.dto.UpdatePolicyResponseDTO;
import com.sk.test.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;

@RestController
@RequiredArgsConstructor
public class PolicyController implements PolicyApi {

    private final PolicyService policyService;

    @Override
    public CreatePolicyResponseDTO createPolicy(@Nonnull final CreatePolicyRequestDTO createPolicyDTO) {
        return policyService.createPolicy(createPolicyDTO);
    }

    @Override
    public UpdatePolicyResponseDTO updatePolicy(@Nonnull final UpdatePolicyRequestDTO updatePolicy) {
        return policyService.updatePolicy(updatePolicy);
    }

    @Override
    public FindPolicyResponseDTO findPolicy(@Nonnull final FindPolicyRequestDTO createPolicyDTO) {
        return policyService.findPolicy(createPolicyDTO);
    }
}
