package com.sk.test.service.enricher;

import com.sk.test.service.PolicyEnricher;
import com.sk.test.service.UUIDGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
@Order(value = 4)
@RequiredArgsConstructor
public class PolicyIdEnricher implements PolicyEnricher {

    @Override
    public boolean canEnrich(@Nonnull final PolicyEnricherContext context) {
        return Objects.isNull(context.getPolicyDocument().getPolicyId());
    }

    @Override
    public void enrich(@Nonnull final PolicyEnricherContext context) {
        context.getPolicyDocument().setPolicyId(UUIDGenerator.generate());
    }
}
