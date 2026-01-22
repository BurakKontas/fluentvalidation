package tr.kontas.fluentvalidation.annotations;

import tr.kontas.fluentvalidation.validation.Validator;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
    Class<? extends Validator<?>> validator();
}