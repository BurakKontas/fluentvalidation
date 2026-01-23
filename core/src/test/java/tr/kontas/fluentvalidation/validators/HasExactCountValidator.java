package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class HasExactCountValidator extends Validator<TestModel> {
    public HasExactCountValidator(int count) {
        ruleFor(TestModel::getList).hasExactCount(count);
    }
}
