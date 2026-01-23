package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ConditionalValidationValidator extends Validator<TestModel> {
    public ConditionalValidationValidator() {
        ruleFor(TestModel::getAge)
                .when(TestModel::isActive)
                .greaterThanOrEqualTo(18);

        ruleFor(TestModel::getValue)
                .unless(TestModel::isActive)
                .isNull();
    }
}
