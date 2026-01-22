package tr.kontas.fluentvalidation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationResult {

    private final List<String> errors = new ArrayList<>();

    public void addError(String error) {
        errors.add(error);
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public boolean isNotValid() {
        return !isValid();
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        if (isValid()) {
            return "ValidationResult: valid";
        }

        return "ValidationResult: invalid\n" +
                errors.stream()
                        .map(e -> " - " + e)
                        .collect(Collectors.joining("\n"));
    }
}
