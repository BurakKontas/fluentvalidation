package tr.kontas.fluentvalidation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ValidateAspect {

    @After("execution(*.new(..)) && @within(validate)")
    public void afterCtor(JoinPoint jp, Validate validate) throws Throwable {

        Object target = jp.getThis();

        Class<? extends Validator<?>> validatorClass = validate.validator();

        Validator validator = validatorClass.getDeclaredConstructor().newInstance();

        ValidationResult result = validator.validate(target);

        if (result.isNotValid()) {
            throw new FluentValidationException(result);
        }
    }
}
