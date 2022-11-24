package com.sk.test.service.enricher;

import com.sk.test.service.PolicyEnricher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.LocalDate;

@Service
@Order(value = 5)
public class PolicyExpireDateEnricher implements PolicyEnricher {

    private static final int DAYS_TO_ADD = 30;

    @Override
    public boolean canEnrich(@Nonnull final PolicyEnricherContext context) {
        return true;
    }

    @Override
    public void enrich(@Nonnull final PolicyEnricherContext context) {
        context.getPolicyDocument().setExpireDate(LocalDate.now().plusDays(DAYS_TO_ADD));
    }

}
