package tr.kontas.fluentvalidation.spring;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import tr.kontas.fluentvalidation.*;

public class FluentValidationEntityListener {

    @PrePersist
    @PreUpdate
    public void validate(Object entity) {

        if (!SpringContextHolder.isJpaEnabled()) {
            return; // skip if JPA integration disabled
        }

        Validate validate = entity.getClass().getAnnotation(Validate.class);
        if (validate == null) return;

        Class<? extends Validator<?>> validatorClass = validate.validator();
        Validator validator = SpringContextHolder.getBean(validatorClass);

        ValidationResult result = validator.validate(entity);
        if (result.isNotValid()) {
            throw new FluentValidationException(result, entity.getClass());
        }
    }
}
