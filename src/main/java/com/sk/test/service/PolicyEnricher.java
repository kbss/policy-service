package com.sk.test.service;

import com.sk.test.service.enricher.PolicyEnricherContext;

public interface PolicyEnricher {

    boolean canEnrich(PolicyEnricherContext context);

    void enrich(PolicyEnricherContext context);

}
