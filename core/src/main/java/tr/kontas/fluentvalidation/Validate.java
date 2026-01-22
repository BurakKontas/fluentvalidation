package tr.kontas.fluentvalidation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
    Class<? extends Validator<?>> validator();
}