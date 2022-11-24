package com.sk.test.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("policy")
@CompoundIndexes(@CompoundIndex(name = "ixd_policy_order", def = "{'policyId' : 1, 'effectiveDate': 1, 'expireDate': 1, 'active': 1}", unique = true))
public class PolicyDocument {

    @Version
    Integer version;
    @Id
    private String id;
    private String policyId;
    private LocalDate startDate;
    private LocalDate expireDate;
    private Boolean active;
    private List<PersonDocument> insuredPersons;
    private BigDecimal totalPremium;
    @CreatedDate
    private LocalDateTime createdOn;
}
