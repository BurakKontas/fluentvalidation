package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreditCardValidator extends Validator<TestModel> {
    public CreditCardValidator() {
        ruleFor(TestModel::getValue).creditCard();
    }
}
