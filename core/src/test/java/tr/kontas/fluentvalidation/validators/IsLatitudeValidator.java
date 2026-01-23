package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsLatitudeValidator extends Validator<TestModel> {
    public IsLatitudeValidator() {
        ruleFor(TestModel::getLatitude).isLatitude();
    }
}
