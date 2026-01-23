package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class EndsWithValidator extends Validator<TestModel> {
    public EndsWithValidator(String suffix) {
        ruleFor(TestModel::getValue).endsWith(suffix);
    }
}
