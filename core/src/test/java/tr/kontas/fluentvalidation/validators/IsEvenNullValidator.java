package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsEvenNullValidator extends Validator<TestModel> {
    public IsEvenNullValidator() {
        ruleFor(TestModel::getAge).isEven();
    }
}
