package com.sk.test.service.enricher;

import com.sk.test.service.PolicyEnricher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
@Order(value = 1)
public class PolicyNewPersonsEnricher implements PolicyEnricher {

    @Override
    public boolean canEnrich(@Nonnull final PolicyEnricherContext context) {
        return Objects.nonNull(context.getCreatePolicyDTO());
    }

    @Override
    public void enrich(@Nonnull final PolicyEnricherContext context) {
        context.getCreatePolicyDTO().getInsuredPersons().forEach(p -> p.setId(null));
    }

}
