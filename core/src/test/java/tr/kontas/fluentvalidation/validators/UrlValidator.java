package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class UrlValidator extends Validator<TestModel> {
    public UrlValidator() {
        ruleFor(TestModel::getValue).url();
    }
}
