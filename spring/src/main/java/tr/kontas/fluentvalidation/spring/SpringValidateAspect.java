package tr.kontas.fluentvalidation.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import tr.kontas.fluentvalidation.exceptions.FluentValidationException;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.validation.ValidationResult;
import tr.kontas.fluentvalidation.validation.Validator;

@Aspect
@ConditionalOnClass(ApplicationContext.class)
public class SpringValidateAspect implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    @Before("execution(* (@org.springframework.web.bind.annotation.RestController *).*(..))")
    public void beforeController(JoinPoint joinPoint) {

        for (Object arg : joinPoint.getArgs()) {
            if (arg == null) continue;

            Validate validate = arg.getClass().getAnnotation(Validate.class);
            if (validate == null) continue;

            Class<? extends Validator<?>> validatorClass = validate.validator();
            Validator validator = applicationContext.getBean(validatorClass);

            ValidationResult result = validator.validate(arg);
            if (result.isNotValid()) {
                throw new FluentValidationException(result, arg.getClass());
            }
        }
    }
}


