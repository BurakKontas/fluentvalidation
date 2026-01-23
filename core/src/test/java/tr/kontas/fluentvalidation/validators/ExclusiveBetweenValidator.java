package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ExclusiveBetweenValidator extends Validator<TestModel> {
    public ExclusiveBetweenValidator(int min, int max) {
        ruleFor(TestModel::getAge).exclusiveBetween(min, max);
    }
}
