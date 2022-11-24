package com.sk.test.service;

import com.sk.test.service.enricher.PolicyEnricherContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyEnricherService {

    private final List<PolicyEnricher> enrichers;

    public void enrich(PolicyEnricherContext context) {
        enrichers.stream().filter(e -> e.canEnrich(context)).forEach(e -> {
            log.trace("Enriching with: {}", e.getClass().getName());
            e.enrich(context);
        });
    }
}
