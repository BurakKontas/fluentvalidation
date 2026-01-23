package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsCamelCaseValidator extends Validator<TestModel> {
    public IsCamelCaseValidator() {
        ruleFor(TestModel::getValue).isCamelCase();
    }
}
