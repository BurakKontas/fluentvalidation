package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsDivisibleByValidator extends Validator<TestModel> {
    public IsDivisibleByValidator(int divisor) {
        ruleFor(TestModel::getAge).isDivisibleBy(divisor);
    }
}
