package tr.kontas.fluentvalidation.validation;

import tr.kontas.fluentvalidation.dtos.FieldError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationResult {

    private final List<FieldError> errors = new ArrayList<>();

    public void addError(String property, String message) {
        errors.add(new FieldError(property, message));
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public boolean isNotValid() {
        return !isValid();
    }

    public List<FieldError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    @Override
    public String toString() {
        if (isValid()) {
            return "ValidationResult: valid";
        }

        return "ValidationResult: invalid\n" +
                errors.stream()
                        .map(e -> " - %s: %s".formatted(
                                e.property(),
                                e.message()
                        ))
                        .collect(Collectors.joining("\n"));
    }
}
