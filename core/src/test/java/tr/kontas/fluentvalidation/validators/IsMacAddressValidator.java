package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsMacAddressValidator extends Validator<TestModel> {
    public IsMacAddressValidator() {
        ruleFor(TestModel::getValue).isMacAddress();
    }
}
