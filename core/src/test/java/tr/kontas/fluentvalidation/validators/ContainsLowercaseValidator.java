package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ContainsLowercaseValidator extends Validator<TestModel> {
    public ContainsLowercaseValidator() {
        ruleFor(TestModel::getValue).containsLowercase();
    }
}
