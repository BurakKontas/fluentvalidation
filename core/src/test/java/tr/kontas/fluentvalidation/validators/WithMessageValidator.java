package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class WithMessageValidator extends Validator<TestModel> {
    public WithMessageValidator() {
        ruleFor(TestModel::getValue)
                .notNull()
                .withMessage("Custom error message");
    }
}
