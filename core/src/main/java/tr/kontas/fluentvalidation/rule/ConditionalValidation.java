package tr.kontas.fluentvalidation.rule;

import tr.kontas.fluentvalidation.enums.CascadeMode;

import java.util.function.Predicate;

public class ConditionalValidation<T, R> extends RuleBuilder<T, R> {

    private final RuleBuilder<T, R> parentBuilder;
    private final Predicate<T> condition;
    private final boolean shouldValidateWhenTrue;

    public ConditionalValidation(RuleBuilder<T, R> parentBuilder,
                                 Predicate<T> condition,
                                 boolean shouldValidateWhenTrue) {
        super(parentBuilder.getPropertyName(), parentBuilder.getPropertyAccessor());
        this.parentBuilder = parentBuilder;
        this.condition = condition;
        this.shouldValidateWhenTrue = shouldValidateWhenTrue;
        this.cascade(parentBuilder.getCascadeMode());
    }

    @Override
    public ConditionalValidation<T, R> must(Predicate<R> predicate, String message) {
        parentBuilder.mustWithEntityCondition(condition, shouldValidateWhenTrue, predicate, message);

        return this;
    }

    @Override
    public RuleBuilder<T, R> withMessage(String message) {
        parentBuilder.withMessage(message);
        return parentBuilder;
    }

    @Override
    public ConditionalValidation<T, R> when(Predicate<T> newCondition) {
        return parentBuilder.when(newCondition);
    }

    @Override
    public ConditionalValidation<T, R> unless(Predicate<T> newCondition) {
        return parentBuilder.unless(newCondition);
    }

    @Override
    public RuleBuilder<T, R> cascade(CascadeMode mode) {
        parentBuilder.cascade(mode);
        return parentBuilder;
    }
}