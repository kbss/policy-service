package com.sk.test.repository;

import com.sk.test.domain.PolicyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface PolicyRepository extends MongoRepository<PolicyDocument, String> {

    @Query("""
                {
                    policyId: '?0',
                    active: true,
                    startDate: { $lte : { $date : '?1'}},
                    expireDate: { $gte : { $date : '?1'}},
                }
            """)
    Optional<PolicyDocument> findActivePolicy(String policyId, LocalDate requestDate);

    @Query("""
                {
                    policyId: '?0',
                    active: true
                }
            """)
    Optional<PolicyDocument> findActivePolicy(String policyId);

}
