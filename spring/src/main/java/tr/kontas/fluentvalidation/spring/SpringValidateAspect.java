package tr.kontas.fluentvalidation.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import tr.kontas.fluentvalidation.FluentValidationException;
import tr.kontas.fluentvalidation.Validate;
import tr.kontas.fluentvalidation.ValidationResult;
import tr.kontas.fluentvalidation.Validator;

@Aspect
@Component
@ConditionalOnClass(ApplicationContext.class)
public class SpringValidateAspect implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public SpringValidateAspect() {
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) {
        applicationContext = context;
    }

    @After("execution(*.new(..)) && @within(validate)")
    public void afterCtor(JoinPoint jp, Validate validate) {

        Object target = jp.getThis();

        Class<? extends Validator<?>> validatorClass = validate.validator();

        Validator validator = applicationContext.getBean(validatorClass);

        ValidationResult result = validator.validate(target);

        if (result.isNotValid()) {
            throw new FluentValidationException(result);
        }
    }
}

