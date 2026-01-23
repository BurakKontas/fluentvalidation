package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsOddNullValidator extends Validator<TestModel> {
    public IsOddNullValidator() {
        ruleFor(TestModel::getAge).isOdd();
    }
}
