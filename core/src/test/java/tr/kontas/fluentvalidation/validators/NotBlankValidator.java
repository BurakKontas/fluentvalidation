package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class NotBlankValidator extends Validator<TestModel> {
    public NotBlankValidator() {
        ruleFor(TestModel::getValue).notBlank();
    }
}
