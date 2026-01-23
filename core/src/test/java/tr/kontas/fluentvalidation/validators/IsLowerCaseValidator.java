package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsLowerCaseValidator extends Validator<TestModel> {
    public IsLowerCaseValidator() {
        ruleFor(TestModel::getValue).isLowerCase();
    }
}
