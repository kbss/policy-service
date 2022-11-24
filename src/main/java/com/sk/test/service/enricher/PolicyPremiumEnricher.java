package com.sk.test.service.enricher;

import com.sk.test.domain.PersonDocument;
import com.sk.test.service.PolicyEnricher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

@Service
@Order(value = 2)
public class PolicyPremiumEnricher implements PolicyEnricher {

    @Override
    public boolean canEnrich(@Nonnull final PolicyEnricherContext context) {
        return true;
    }

    @Override
    public void enrich(@Nonnull final PolicyEnricherContext context) {
        BigDecimal totalPremium = context.getPolicyDocument()
                .getInsuredPersons()
                .stream()
                .map(PersonDocument::getPremium)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        context.getPolicyDocument().setTotalPremium(totalPremium);
    }
}
