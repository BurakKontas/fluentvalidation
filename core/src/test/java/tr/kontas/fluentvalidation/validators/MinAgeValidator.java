package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class MinAgeValidator extends Validator<TestModel> {
    public MinAgeValidator(int years) {
        ruleFor(TestModel::getBirthDate).minAge(years);
    }
}
