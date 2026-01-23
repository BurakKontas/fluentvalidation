package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsIsbnValidator extends Validator<TestModel> {
    public IsIsbnValidator() {
        ruleFor(TestModel::getValue).isIsbn();
    }
}
