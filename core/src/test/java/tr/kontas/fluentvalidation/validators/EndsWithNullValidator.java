package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class EndsWithNullValidator extends Validator<TestModel> {
    public EndsWithNullValidator() {
        ruleFor(TestModel::getValue).endsWith("test");
    }
}
