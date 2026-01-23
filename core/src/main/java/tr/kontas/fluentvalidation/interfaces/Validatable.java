package tr.kontas.fluentvalidation.interfaces;

import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.exceptions.FluentValidationException;
import tr.kontas.fluentvalidation.validation.ValidationResult;
import tr.kontas.fluentvalidation.validation.Validator;

public interface Validatable {
    default ValidationResult validate(boolean throwException) {
        Validate validate = this.getClass().getAnnotation(Validate.class);

        if (validate == null) {
            throw new IllegalStateException(
                    "@Validate annotation not found: " + this.getClass().getName()
            );
        }

        Class<? extends Validator<?>> validatorClass = validate.validator();

        Validator validator;

        try {
            validator = validatorClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create new instance of " + validatorClass.getSimpleName());
        }

        ValidationResult result = validator.validate(this);

        if(result.isNotValid() && throwException) {
            throw new FluentValidationException(result, this.getClass());
        }

        return result;
    }

    default ValidationResult validate() {
        return validate(true);
    }
}
