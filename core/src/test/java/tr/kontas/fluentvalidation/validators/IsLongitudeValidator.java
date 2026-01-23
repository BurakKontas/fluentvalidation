package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsLongitudeValidator extends Validator<TestModel> {
    public IsLongitudeValidator() {
        ruleFor(TestModel::getLongitude).isLongitude();
    }
}
