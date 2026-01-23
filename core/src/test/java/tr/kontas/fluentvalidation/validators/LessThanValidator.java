package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class LessThanValidator extends Validator<TestModel> {
    public LessThanValidator(int value) {
        ruleFor(TestModel::getAge).lessThan(value);
    }
}
