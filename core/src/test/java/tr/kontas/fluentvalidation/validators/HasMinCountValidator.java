package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

// Collection Rules
public class HasMinCountValidator extends Validator<TestModel> {
    public HasMinCountValidator(int min) {
        ruleFor(TestModel::getList).hasMinCount(min);
    }
}
