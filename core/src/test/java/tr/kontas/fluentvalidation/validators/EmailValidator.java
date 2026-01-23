package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class EmailValidator extends Validator<TestModel> {
    public EmailValidator() {
        ruleFor(TestModel::getValue).email();
    }
}
