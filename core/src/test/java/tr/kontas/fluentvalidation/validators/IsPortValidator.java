package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsPortValidator extends Validator<TestModel> {
    public IsPortValidator() {
        ruleFor(TestModel::getPort).isPort();
    }
}
