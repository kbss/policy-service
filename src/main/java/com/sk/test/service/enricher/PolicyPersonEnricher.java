package com.sk.test.service.enricher;

import com.sk.test.domain.PersonDocument;
import com.sk.test.service.PolicyEnricher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.List;

@Service
@Order(value = 1)
public class PolicyPersonEnricher implements PolicyEnricher {

    @Override
    public boolean canEnrich(@Nonnull final PolicyEnricherContext context) {
        return !CollectionUtils.isEmpty(context.getPersons());
    }

    @Override
    public void enrich(@Nonnull final PolicyEnricherContext context) {
        List<PersonDocument> persons = context.getPersons().stream().map(p -> PersonDocument.builder()
                .firstName(p.getFirstName())
                .secondName(p.getSecondName())
                .premium(p.getPremium())
                .build()
        ).toList();
        context.getPolicyDocument().setInsuredPersons(persons);
    }

}
