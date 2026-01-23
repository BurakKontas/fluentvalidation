package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsPercentageValidator extends Validator<TestModel> {
    public IsPercentageValidator() {
        ruleFor(TestModel::getPercentage).isPercentage();
    }
}
