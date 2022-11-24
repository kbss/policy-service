package com.sk.test.service;

import com.sk.test.domain.PolicyDocument;
import com.sk.test.dto.CreatePolicyRequestDTO;
import com.sk.test.dto.CreatePolicyResponseDTO;
import com.sk.test.dto.FindPolicyRequestDTO;
import com.sk.test.dto.FindPolicyResponseDTO;
import com.sk.test.dto.InsuredPersonDTO;
import com.sk.test.dto.UpdatePolicyRequestDTO;
import com.sk.test.dto.UpdatePolicyResponseDTO;
import com.sk.test.error.exception.NotFoundException;
import com.sk.test.error.exception.PolicyException;
import com.sk.test.helper.PolicyHelper;
import com.sk.test.repository.PolicyRepository;
import com.sk.test.service.enricher.PolicyEnricherContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;

    private final PolicyEnricherService policyEnricherService;

    @Nonnull
    public CreatePolicyResponseDTO createPolicy(@Nonnull final CreatePolicyRequestDTO createPolicyDTO) {
        log.info("Creating new policy");
        log.trace("Request body: {}", createPolicyDTO);
        validateStartDateInFuture(createPolicyDTO);
        PolicyDocument policy = createNewPolicy();
        enrichAndSave(policy, createPolicyDTO.getInsuredPersons());
        CreatePolicyResponseDTO response = CreatePolicyResponseDTO.builder().startDate(policy.getStartDate()).build();
        PolicyHelper.update(response, policy);
        return response;
    }

    private void validateStartDateInFuture(CreatePolicyRequestDTO createPolicyDTO) {
        LocalDate now = LocalDate.now();
        if (now.isAfter(createPolicyDTO.getStartDate())) {
            throw new PolicyException("Start date can't be in future");
        } else if (now.isBefore(createPolicyDTO.getStartDate())) {
            throw new PolicyException("Start date can't be in past");
        }
    }

    private PolicyDocument createNewPolicy() {
        return PolicyDocument.builder()
                .startDate(LocalDate.now())
                .active(true)
                .build();
    }

    @Nonnull
    @Transactional
    public UpdatePolicyResponseDTO updatePolicy(@Nonnull final UpdatePolicyRequestDTO updatePolicy) {
        log.info("Updating policy: {}", updatePolicy.getPolicyId());

        validateEffectiveDate(updatePolicy);
        PolicyDocument activePolicy = policyRepository.findActivePolicy(updatePolicy.getPolicyId())
                .orElseThrow(this::buildNotFoundException);
        log.info("Disabling policy: {}", activePolicy.getId());

        activePolicy.setActive(false);
        policyRepository.save(activePolicy);
        PolicyDocument updatedPolicy = createNewPolicy();
        updatedPolicy.setPolicyId(updatePolicy.getPolicyId());

        enrichAndSave(updatedPolicy, updatePolicy.getInsuredPersons());

        UpdatePolicyResponseDTO response = UpdatePolicyResponseDTO.builder().effectiveDate(updatePolicy.getEffectiveDate()).build();
        PolicyHelper.update(response, updatedPolicy);
        return response;
    }

    private void validateEffectiveDate(UpdatePolicyRequestDTO updatePolicy) {
        if (LocalDate.now().isAfter(updatePolicy.getEffectiveDate())) {
            throw new PolicyException("Effective date can't be in past");
        }
    }

    private void enrichAndSave(PolicyDocument policy, List<InsuredPersonDTO> updatePolicy) {
        policyEnricherService.enrich(PolicyEnricherContext.builder().policyDocument(policy).persons(updatePolicy).build());
        policyRepository.save(policy);
    }

    private NotFoundException buildNotFoundException() {
        return new NotFoundException("Active policy not found");
    }

    @Nonnull
    public FindPolicyResponseDTO findPolicy(@Nonnull final FindPolicyRequestDTO findPolicyRequest) {
        log.info("Searching policy, request: [{}]", findPolicyRequest);
        LocalDate requestDate = Optional.ofNullable(findPolicyRequest.getRequestDate()).orElse(LocalDate.now());
        PolicyDocument activePolicy = policyRepository.findActivePolicy(findPolicyRequest.getPolicyId(), requestDate).orElseThrow(this::buildNotFoundException);
        FindPolicyResponseDTO response = FindPolicyResponseDTO.builder().requestDate(requestDate).build();
        PolicyHelper.update(response, activePolicy);
        return response;
    }
}
