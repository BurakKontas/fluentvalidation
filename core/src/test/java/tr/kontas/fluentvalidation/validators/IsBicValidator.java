package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsBicValidator extends Validator<TestModel> {
    public IsBicValidator() {
        ruleFor(TestModel::getValue).isBic();
    }
}
