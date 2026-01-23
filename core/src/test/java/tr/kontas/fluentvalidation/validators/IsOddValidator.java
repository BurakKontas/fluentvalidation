package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsOddValidator extends Validator<TestModel> {
    public IsOddValidator() {
        ruleFor(TestModel::getAge).isOdd();
    }
}
