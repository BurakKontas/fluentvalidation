package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsNumericValidator extends Validator<TestModel> {
    public IsNumericValidator() {
        ruleFor(TestModel::getValue).isNumeric();
    }
}
