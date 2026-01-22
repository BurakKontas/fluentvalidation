package tr.kontas.fluentvalidation;

public class FluentValidationException extends RuntimeException {

    private final ValidationResult result;
    private final String entityName;

    public FluentValidationException(ValidationResult result, Class<?> entityClass) {
        super(buildMessage(result, entityClass));
        this.result = result;
        this.entityName = entityClass.getSimpleName();
    }

    public ValidationResult getResult() {
        return result;
    }

    @Override
    public String getMessage() {
        return buildMessage(result, entityName);
    }

    public String getEntityName() {
        return this.entityName;
    }

    @Override
    public String toString() {
        return getMessage();
    }

    private static String buildMessage(ValidationResult result, Class<?> entityClass) {
        return buildMessage(result, entityClass.getSimpleName().toLowerCase());
    }

    private static String buildMessage(ValidationResult result, String entityName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Validation failed for ").append(entityName).append("\n");
        for (FieldError e : result.getErrors()) {
            sb.append(" - ").append(e.property()).append(": ").append(e.message()).append("\n");
        }
        return sb.toString();
    }
}
