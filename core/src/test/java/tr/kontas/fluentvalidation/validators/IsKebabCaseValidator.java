package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsKebabCaseValidator extends Validator<TestModel> {
    public IsKebabCaseValidator() {
        ruleFor(TestModel::getValue).isKebabCase();
    }
}
