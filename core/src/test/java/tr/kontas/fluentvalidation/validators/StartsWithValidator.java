package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class StartsWithValidator extends Validator<TestModel> {
    public StartsWithValidator(String prefix) {
        ruleFor(TestModel::getValue).startsWith(prefix);
    }
}
