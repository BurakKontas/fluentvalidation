package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ContainsUppercaseValidator extends Validator<TestModel> {
    public ContainsUppercaseValidator() {
        ruleFor(TestModel::getValue).containsUppercase();
    }
}
