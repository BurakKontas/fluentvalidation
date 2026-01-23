package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsAlphaValidator extends Validator<TestModel> {
    public IsAlphaValidator() {
        ruleFor(TestModel::getValue).isAlpha();
    }
}
