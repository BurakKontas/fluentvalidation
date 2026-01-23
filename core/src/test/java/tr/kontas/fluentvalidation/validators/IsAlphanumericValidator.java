package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsAlphanumericValidator extends Validator<TestModel> {
    public IsAlphanumericValidator() {
        ruleFor(TestModel::getValue).isAlphanumeric();
    }
}
