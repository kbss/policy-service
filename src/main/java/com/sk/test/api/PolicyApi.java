package com.sk.test.api;

import com.sk.test.dto.CreatePolicyRequestDTO;
import com.sk.test.dto.CreatePolicyResponseDTO;
import com.sk.test.dto.FindPolicyRequestDTO;
import com.sk.test.dto.FindPolicyResponseDTO;
import com.sk.test.dto.UpdatePolicyRequestDTO;
import com.sk.test.dto.UpdatePolicyResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface PolicyApi {

    String VERSION = "v1";

    @Operation(
            operationId = "createPolicy",
            summary = "Create a new policy",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Expected response to a valid request", content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CreatePolicyResponseDTO.class))
                    })
            }
    )
    @PostMapping(path = VERSION + "/policy", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CreatePolicyResponseDTO createPolicy(@Nonnull final @Valid @RequestBody CreatePolicyRequestDTO createPolicyDTO);

    @Operation(
            operationId = "createPolicy",
            summary = "Create a new policy",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Expected response to a valid request", content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UpdatePolicyResponseDTO.class))
                    })
            }
    )
    @PutMapping(path = VERSION + "/policy", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    UpdatePolicyResponseDTO updatePolicy(@Nonnull final @Valid @RequestBody UpdatePolicyRequestDTO updatePolicy);

    @Operation(
            operationId = "findPolicy",
            summary = "Search policy by given criteria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Expected response to a valid request", content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = FindPolicyResponseDTO.class))
                    })
            }
    )
    @PostMapping(path = VERSION + "/policy/find", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    FindPolicyResponseDTO findPolicy(@Nonnull final @RequestBody FindPolicyRequestDTO createPolicyDTO);
}
