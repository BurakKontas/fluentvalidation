package tr.kontas.fluentvalidation.spring.example.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tr.kontas.fluentvalidation.FieldError;
import tr.kontas.fluentvalidation.FluentValidationException;
import tr.kontas.fluentvalidation.ValidationResult;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class FluentValidationExceptionAdvice {

    private static final URI VALIDATION_ERROR_TYPE = URI.create(
            "https://tr.kontas/validation-error"
    );

    @ExceptionHandler(FluentValidationException.class)
    public ProblemDetail handleFluentValidationException(FluentValidationException ex) {

        ValidationResult result = ex.getResult();
        String entityName = ex.getEntityName();

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setType(VALIDATION_ERROR_TYPE);
        problem.setTitle("Validation Failed");
        problem.setDetail("One or more validation errors occurred");

        Map<String, List<FieldError>> errors =
                Map.of(entityName,
                        result.getErrors().stream()
                                .map(f -> new FieldError(
                                        f.property(),
                                        f.message()
                                ))
                                .toList()
                );

        problem.setProperty("errors", errors);

        return problem;
    }
}