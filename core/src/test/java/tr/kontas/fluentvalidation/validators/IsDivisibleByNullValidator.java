package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsDivisibleByNullValidator extends Validator<TestModel> {
    public IsDivisibleByNullValidator() {
        ruleFor(TestModel::getAge).isDivisibleBy(2);
    }
}
