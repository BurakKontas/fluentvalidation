package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsEmptyValidator extends Validator<TestModel> {
    public IsEmptyValidator() {
        ruleFor(TestModel::getValue).isEmpty();
    }
}
