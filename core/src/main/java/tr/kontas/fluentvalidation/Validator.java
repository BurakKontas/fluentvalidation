package tr.kontas.fluentvalidation;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class Validator<T> {

    private final List<RuleBuilder<T, ?>> rules = new ArrayList<>();

    protected <R> RuleBuilder<T, R> ruleFor(SerializableFunction<T, R> property) {
        String propertyName = resolvePropertyName(property);
        RuleBuilder<T, R> rule = new RuleBuilder<>(propertyName, property);
        rules.add(rule);
        return rule;
    }

    public ValidationResult validate(T target) {
        ValidationResult result = new ValidationResult();
        if(skip(target)) return result;

        for (RuleBuilder<T, ?> rule : rules) {
            rule.validate(target, result);
        }
        return result;
    }

    private String resolvePropertyName(SerializableFunction<T, ?> fn) {
        try {
            Method writeReplace = fn.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);

            SerializedLambda lambda =
                    (SerializedLambda) writeReplace.invoke(fn);

            String methodName = lambda.getImplMethodName();
            return extractPropertyName(methodName);

        } catch (Exception e) {
            throw new RuntimeException("Property name çözümlenemedi", e);
        }
    }

    private String extractPropertyName(String methodName) {
        if (methodName.startsWith("get")) {
            return decapitalize(methodName.substring(3));
        }
        if (methodName.startsWith("is")) {
            return decapitalize(methodName.substring(2));
        }

        // record / custom accessor fallback
        return methodName;
    }

    private String decapitalize(String value) {
        return Character.toLowerCase(value.charAt(0)) + value.substring(1);
    }

    public boolean skip(T entity) {
        return false;
    }
}
