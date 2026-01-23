package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsInFutureValidator extends Validator<TestModel> {
    public IsInFutureValidator() {
        ruleFor(TestModel::getBirthDate).isInFuture();
    }
}
