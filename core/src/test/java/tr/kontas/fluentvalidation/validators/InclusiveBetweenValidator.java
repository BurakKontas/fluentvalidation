package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class InclusiveBetweenValidator extends Validator<TestModel> {
    public InclusiveBetweenValidator(int min, int max) {
        ruleFor(TestModel::getAge).inclusiveBetween(min, max);
    }
}
