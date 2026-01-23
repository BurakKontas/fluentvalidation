package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class LessThanNullValidator extends Validator<TestModel> {
    public LessThanNullValidator() {
        ruleFor(TestModel::getAge).lessThan(100);
    }
}
