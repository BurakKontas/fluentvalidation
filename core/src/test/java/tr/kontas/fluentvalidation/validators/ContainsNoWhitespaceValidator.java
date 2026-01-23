package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ContainsNoWhitespaceValidator extends Validator<TestModel> {
    public ContainsNoWhitespaceValidator() {
        ruleFor(TestModel::getValue).containsNoWhitespace();
    }
}
