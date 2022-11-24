package com.sk.test.error;

import com.sk.test.dto.ErrorDTO;
import com.sk.test.error.exception.NotFoundException;
import com.sk.test.error.exception.PolicyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerAdvice {

    private final Tracer tracer;

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorDTO handleException(@Nonnull final Throwable throwable) {
        return buildError(throwable);
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(@Nonnull final NotFoundException throwable) {
        return buildError(throwable);
    }

    @ResponseBody
    @ExceptionHandler(PolicyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handlePolicyException(@Nonnull final PolicyException throwable) {
        return buildError(throwable);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleValidationException(@Nonnull final MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        return getBuilder().message(message).build();
    }

    private ErrorDTO buildError(@Nonnull final Throwable throwable) {
        log.error("Error: {}", throwable.getMessage(), throwable);
        return getBuilder().message(throwable.getMessage()).build();
    }

    private ErrorDTO.ErrorDTOBuilder getBuilder() {
        return ErrorDTO.builder().id(getTraceId());
    }

    private String getTraceId() {
        Span span = tracer.currentSpan();
        if (Objects.nonNull(span)) {
            return span.context().traceId();
        }
        return null;
    }
}
