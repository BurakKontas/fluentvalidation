package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class GreaterThanNullValidator extends Validator<TestModel> {
    public GreaterThanNullValidator() {
        ruleFor(TestModel::getAge).greaterThan(10);
    }
}
