package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ContainsDigitValidator extends Validator<TestModel> {
    public ContainsDigitValidator() {
        ruleFor(TestModel::getValue).containsDigit();
    }
}
