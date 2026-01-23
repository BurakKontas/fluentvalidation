package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ContainsNullValidator extends Validator<TestModel> {
    public ContainsNullValidator() {
        ruleFor(TestModel::getValue).contains("test");
    }
}
