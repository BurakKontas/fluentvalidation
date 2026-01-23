package tr.kontas.fluentvalidation.enums;

/**
 * Defines the cascade mode for validation rules.
 * This enum controls how validation proceeds when a rule fails.
 *
 * <p><b>CONTINUE:</b> Continue validating all rules even if some fail</p>
 * <p><b>STOP:</b> Stop validation at the first failing rule</p>
 *
 * <p><b>Example:</b></p>
 * <pre>
 * {@code
 * // In a validator class:
 * setCascadeMode(CascadeMode.STOP);
 * }
 * </pre>
 */
public enum CascadeMode {
    /**
     * Continue validating all rules even when some rules fail.
     * This mode collects all validation errors.
     */
    CONTINUE,

    /**
     * Stop validation at the first failing rule.
     * This mode provides faster validation but fewer error details.
     */
    STOP
}