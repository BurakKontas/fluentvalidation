package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsPhoneNumberValidator extends Validator<TestModel> {
    public IsPhoneNumberValidator() {
        ruleFor(TestModel::getValue).isPhoneNumber();
    }
}
