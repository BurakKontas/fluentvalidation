package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class MaxAgeValidator extends Validator<TestModel> {
    public MaxAgeValidator(int years) {
        ruleFor(TestModel::getBirthDate).maxAge(years);
    }
}
