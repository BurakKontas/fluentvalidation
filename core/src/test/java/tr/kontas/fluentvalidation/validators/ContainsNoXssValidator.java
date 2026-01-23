package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ContainsNoXssValidator extends Validator<TestModel> {
    public ContainsNoXssValidator() {
        ruleFor(TestModel::getValue).containsNoXss();
    }
}
