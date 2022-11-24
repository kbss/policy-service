package com.sk.test.service.enricher;

import com.sk.test.service.PolicyEnricher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
@Order(value = 4)
public class StartDateEnricher implements PolicyEnricher {

    @Override
    public boolean canEnrich(@Nonnull final PolicyEnricherContext context) {
        return Objects.nonNull(context.getUpdatePolicy());
    }

    @Override
    public void enrich(@Nonnull final PolicyEnricherContext context) {
        context.getPolicyDocument().setStartDate(context.getUpdatePolicy().getEffectiveDate());
    }
}
