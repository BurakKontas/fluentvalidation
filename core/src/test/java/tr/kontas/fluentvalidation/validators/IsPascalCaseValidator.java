package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsPascalCaseValidator extends Validator<TestModel> {
    public IsPascalCaseValidator() {
        ruleFor(TestModel::getValue).isPascalCase();
    }
}
