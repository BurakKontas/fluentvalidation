package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class HasMaxCountValidator extends Validator<TestModel> {
    public HasMaxCountValidator(int max) {
        ruleFor(TestModel::getList).hasMaxCount(max);
    }
}
