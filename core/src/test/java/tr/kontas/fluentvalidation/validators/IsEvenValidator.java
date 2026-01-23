package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsEvenValidator extends Validator<TestModel> {
    public IsEvenValidator() {
        ruleFor(TestModel::getAge).isEven();
    }
}
