package com.sk.test.Test;

import com.sk.test.TestApplication;
import com.sk.test.domain.PersonDocument;
import com.sk.test.domain.PolicyDocument;
import com.sk.test.dto.CreatePolicyRequestDTO;
import com.sk.test.dto.CreatePolicyResponseDTO;
import com.sk.test.dto.FindPolicyRequestDTO;
import com.sk.test.dto.InsuredPersonDTO;
import com.sk.test.dto.UpdatePolicyRequestDTO;
import com.sk.test.repository.PolicyRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PolicyIntegrationTest {

    static {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Value("http://localhost:${local.server.port}")
    private String baseUrl;

    @Autowired
    private PolicyRepository policyRepository;

    private static List<Arguments> invalidStartDates() {
        return List.of(
                arguments(LocalDate.now().plusDays(1), "Start date can't be in past"),
                arguments(LocalDate.now().minusDays(1), "Start date can't be in future")
        );
    }

    private static List<Arguments> requestDatesSource() {
        return List.of(
                arguments(LocalDate.now()),
                arguments(LocalDate.now().plusDays(10)),
                arguments(LocalDate.now().plusDays(25))
        );
    }

    private static List<Arguments> requestInvalidDatesSource() {
        return List.of(
                arguments(LocalDate.now().minusDays(1)),
                arguments(LocalDate.now().plusDays(31))
        );
    }

    @BeforeEach
    void clean() {
        policyRepository.deleteAll();
    }

    @Test
    @DisplayName("Create a new policy")
    void createNewPolicyTest() {
        CreatePolicyRequestDTO request = buildCreatePolicyRequest(LocalDate.now());
        //@formatter:off
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post(baseUrl+"/v1/policy")
            .prettyPeek()
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("policyId", is(notNullValue()))
            .body("totalPremium", is(26.49F))
            .body("startDate", is("2022-11-24"))
            .body("insuredPersons[0].id", is(1))
            .body("insuredPersons[0].firstName", is("First1"))
            .body("insuredPersons[0].secondName", is("Second1"))
            .body("insuredPersons[0].premium", is(10.5F))
            .body("insuredPersons[1].id", is(2))
            .body("insuredPersons[1].firstName", is("First2"))
            .body("insuredPersons[1].secondName", is("Second2"))
            .body("insuredPersons[1].premium", is(15.99F));
        //@formatter:on
        assertRepositoryNotEmpty();
    }

    @ParameterizedTest
    @MethodSource("invalidStartDates")
    @DisplayName("Create a new policy with invalid start date")
    void createNewPolicyWithInvalidStartDataTest(LocalDate startDate, String message) {
        CreatePolicyRequestDTO request = buildCreatePolicyRequest(startDate);
        createNewPolicy(request).statusCode(400).body("message", is(message));
        assertThat(policyRepository.findAll(), is(empty()));
    }

    @ParameterizedTest
    @MethodSource("requestDatesSource")
    @DisplayName("Find existing policy")
    void findExistingPolicyTest(LocalDate requestDate) {
        CreatePolicyRequestDTO request = buildCreatePolicyRequest(LocalDate.now());
        createNewPolicy(request).statusCode(200);

        List<PolicyDocument> all = assertRepositoryNotEmpty();
        PolicyDocument policyDocument = all.get(0);
        //@formatter:off
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(FindPolicyRequestDTO.builder().policyId(policyDocument.getPolicyId()).requestDate(requestDate).build())
        .when()
            .post(baseUrl + "/v1/policy/find")
            .prettyPeek()
        .then()
            .statusCode(200)
            .body("policyId", is(policyDocument.getPolicyId()))
            .body("totalPremium", is(policyDocument.getTotalPremium().floatValue()))
            .body("requestDate", is(requestDate.toString()))
            .body("insuredPersons[0].id", is(1))
            .body("insuredPersons[0].firstName", is("First1"))
            .body("insuredPersons[0].secondName", is("Second1"))
            .body("insuredPersons[0].premium", is(10.5F))
            .body("insuredPersons[1].id", is(2))
            .body("insuredPersons[1].firstName", is("First2"))
            .body("insuredPersons[1].secondName", is("Second2"))
            .body("insuredPersons[1].premium", is(15.99F));
        //@formatter:on

    }

    @Test
    @DisplayName("Find non existing")
    void findNonExistingPolicyTest() {
        LocalDate requestDate = LocalDate.now();
        CreatePolicyRequestDTO request = buildCreatePolicyRequest(LocalDate.now());
        createNewPolicy(request).statusCode(200);
        assertRepositoryNotEmpty();
        //@formatter:off
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(FindPolicyRequestDTO.builder().policyId("invalidPolicyId").requestDate(requestDate).build())
        .when()
            .post(baseUrl + "/v1/policy/find")
            .prettyPeek()
        .then()
            .statusCode(404)
                .body("message", is("Active policy not found"));
        //@formatter:on

    }

    @ParameterizedTest
    @MethodSource("requestInvalidDatesSource")
    @DisplayName("Find expired policy")
    void findExpiredPolicyTest(LocalDate requestDate) {
        CreatePolicyRequestDTO request = buildCreatePolicyRequest(LocalDate.now());
        createNewPolicy(request).statusCode(200);
        List<PolicyDocument> all = assertRepositoryNotEmpty();
        PolicyDocument policyDocument = all.get(0);
        //@formatter:off
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(FindPolicyRequestDTO.builder().policyId(policyDocument.getPolicyId()).requestDate(requestDate).build())
        .when()
            .post(baseUrl + "/v1/policy/find")
            .prettyPeek()
        .then()
            .statusCode(404)
                .body("message", is("Active policy not found"));
        //@formatter:on
    }


    @Test
    @DisplayName("Update policy")
    void updatePolicyTest() {

        CreatePolicyRequestDTO request = buildCreatePolicyRequest(LocalDate.now());
        CreatePolicyResponseDTO response = createNewPolicy(request).statusCode(200).extract().body().as(CreatePolicyResponseDTO.class);

        List<InsuredPersonDTO> insuredPersons = response.getInsuredPersons();
        insuredPersons.get(0).setPremium(new BigDecimal("10.99"));
        insuredPersons.get(1).setPremium(new BigDecimal("20.99"));
        LocalDate effectiveDate = LocalDate.now().plusDays(1);
        UpdatePolicyRequestDTO requestDTO = UpdatePolicyRequestDTO.builder().effectiveDate(effectiveDate).policyId(response.getPolicyId()).insuredPersons(insuredPersons).build();
        //@formatter:off
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestDTO)
        .when()
            .put(baseUrl + "/v1/policy")
            .prettyPeek()
        .then()
            .statusCode(200)
                .body("policyId", is(response.getPolicyId()))
                .body("effectiveDate", is(effectiveDate.toString()))
                .body("insuredPersons[0].premium", is(10.99F))
                .body("insuredPersons[1].premium", is(20.99F));
        //@formatter:on

    }

    @Test
    @DisplayName("Add new person")
    void addNewPersonTest() {
        CreatePolicyRequestDTO request = buildCreatePolicyRequest(LocalDate.now());
        CreatePolicyResponseDTO response = createNewPolicy(request).statusCode(200).extract().body().as(CreatePolicyResponseDTO.class);
        List<InsuredPersonDTO> insuredPersons = response.getInsuredPersons();
        insuredPersons.add(buildPerson(new BigDecimal("33.55"), "NewFirst", "NewSecond"));

        UpdatePolicyRequestDTO requestDTO = UpdatePolicyRequestDTO.builder().effectiveDate(LocalDate.now()).policyId(response.getPolicyId()).insuredPersons(insuredPersons).build();
        //@formatter:off
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestDTO)
        .when()
            .put(baseUrl + "/v1/policy")
            .prettyPeek()
        .then()
            .statusCode(200)
                .body("policyId", is(response.getPolicyId()))
                .body("insuredPersons", hasSize(3))
                .body("insuredPersons[2].id", is(3))
                .body("insuredPersons[2].firstName", is("NewFirst"))
                .body("insuredPersons[2].secondName", is("NewSecond"))
                .body("insuredPersons[2].premium", is(33.55F))
                .body("totalPremium", is(60.04F));
        //@formatter:on
    }

    @Test
    @DisplayName("Remove existing person")
    void removeExistingPerson() {
        CreatePolicyRequestDTO request = buildCreatePolicyRequest(LocalDate.now());
        CreatePolicyResponseDTO response = createNewPolicy(request).statusCode(200).extract().body().as(CreatePolicyResponseDTO.class);
        List<InsuredPersonDTO> insuredPersons = response.getInsuredPersons();
        insuredPersons.remove(0);

        UpdatePolicyRequestDTO requestDTO = UpdatePolicyRequestDTO.builder().effectiveDate(LocalDate.now()).policyId(response.getPolicyId()).insuredPersons(insuredPersons).build();
        //@formatter:off
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestDTO)
        .when()
            .put(baseUrl + "/v1/policy")
            .prettyPeek()
        .then()
            .statusCode(200)
                .body("policyId", is(response.getPolicyId()))
                .body("insuredPersons", hasSize(1))
                .body("insuredPersons[0].id", is(1))
                .body("insuredPersons[0].firstName", is("First2"))
                .body("insuredPersons[0].secondName", is("Second2"))
                .body("insuredPersons[0].premium", is(15.99F))
                .body("totalPremium", is(15.99F));
        //@formatter:on
    }

    @Test
    @DisplayName("Update policy with effective date in past")
    void updatePolicyWithInvalidEffectiveDate() {
        CreatePolicyRequestDTO request = buildCreatePolicyRequest(LocalDate.now());
        CreatePolicyResponseDTO response = createNewPolicy(request).statusCode(200).extract().body().as(CreatePolicyResponseDTO.class);
        UpdatePolicyRequestDTO requestDTO = UpdatePolicyRequestDTO.builder().effectiveDate(LocalDate.now().minusDays(1)).policyId(response.getPolicyId()).insuredPersons(response.getInsuredPersons()).build();
        //@formatter:off
        RestAssured.given()
            .body(requestDTO)
            .contentType(ContentType.JSON)
        .when()
            .put(baseUrl + "/v1/policy")
            .prettyPeek()
        .then()
            .statusCode(400)
            .body("message", is("Effective date can't be in past"));
        //@formatter:on
    }

    private List<PolicyDocument> assertRepositoryNotEmpty() {
        List<PolicyDocument> all = policyRepository.findAll();
        assertThat(all, not(empty()));
        assertThat(all, hasSize(1));
        PolicyDocument policyDocument = all.get(0);
        assertThat(policyDocument.getPolicyId(), is(notNullValue()));
        assertThat(policyDocument.getId(), is(notNullValue()));
        assertThat(policyDocument.getCreatedOn(), is(notNullValue()));
        assertThat(policyDocument.getActive(), is(true));
        assertThat(policyDocument.getExpireDate(), is(notNullValue()));
        assertThat(policyDocument.getStartDate(), is(notNullValue()));
        assertThat(policyDocument.getInsuredPersons(), hasSize(2));
        List<PersonDocument> insuredPersons = policyDocument.getInsuredPersons();
        insuredPersons.forEach(this::assertPersonNotNull);
        return all;
    }

    private void assertPersonNotNull(PersonDocument person) {
        assertThat(person.getId(), is(notNullValue()));
        assertThat(person.getFirstName(), is(notNullValue()));
        assertThat(person.getSecondName(), is(notNullValue()));
        assertThat(person.getPremium(), is(notNullValue()));
    }

    private ValidatableResponse createNewPolicy(CreatePolicyRequestDTO request) {
        //@formatter:off
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post(baseUrl + "/v1/policy")
            .prettyPeek()
        .then();
        //@formatter:on
    }

    private CreatePolicyRequestDTO buildCreatePolicyRequest(LocalDate startDate) {
        InsuredPersonDTO person1 = buildPerson(new BigDecimal("10.5"), "First1", "Second1");
        InsuredPersonDTO person2 = buildPerson(new BigDecimal("15.99"), "First2", "Second2");
        return CreatePolicyRequestDTO.builder()
                .startDate(startDate)
                .insuredPersons(List.of(person1, person2))
                .build();
    }

    private InsuredPersonDTO buildPerson(BigDecimal premium, String first, String second) {
        return InsuredPersonDTO.builder().premium(premium).firstName(first).secondName(second).build();
    }
}
