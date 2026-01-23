package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

// Comparison Rules
public class EqualToValidator extends Validator<TestModel> {
    public EqualToValidator(String value) {
        ruleFor(TestModel::getValue).equalTo(value);
    }
}
