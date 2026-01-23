package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ArrayHasExactCountValidator extends Validator<TestModel> {
    public ArrayHasExactCountValidator(int count) {
        ruleFor(TestModel::getStringArray).hasExactCount(count);
    }
}
