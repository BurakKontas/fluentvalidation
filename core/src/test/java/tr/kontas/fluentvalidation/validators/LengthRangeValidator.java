package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class LengthRangeValidator extends Validator<TestModel> {
    public LengthRangeValidator(int min, int max) {
        ruleFor(TestModel::getValue).length(min, max);
    }
}
