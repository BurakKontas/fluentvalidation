package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsUpperCaseValidator extends Validator<TestModel> {
    public IsUpperCaseValidator() {
        ruleFor(TestModel::getValue).isUpperCase();
    }
}
