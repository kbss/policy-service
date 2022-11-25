package com.sk.test.service.enricher;

import com.sk.test.domain.PersonDocument;
import com.sk.test.service.PolicyEnricher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Order(value = 3)
public class PolicyPersonIdEnricher implements PolicyEnricher {

    @Override
    public boolean canEnrich(@Nonnull final PolicyEnricherContext context) {
        return !CollectionUtils.isEmpty(context.getPolicyDocument().getInsuredPersons());
    }

    @Override
    public void enrich(@Nonnull final PolicyEnricherContext context) {
        int max = context.getPolicyDocument().getInsuredPersons()
                .stream()
                .filter(p -> Objects.nonNull(p.getId()))
                .mapToInt(PersonDocument::getId)
                .max()
                .orElse(0);
        AtomicInteger idCounter = new AtomicInteger(max);
        context.getPolicyDocument()
                .getInsuredPersons()
                .stream()
                .filter(p -> Objects.isNull(p.getId()))
                .forEach(p -> p.setId(idCounter.incrementAndGet()));
    }

}
