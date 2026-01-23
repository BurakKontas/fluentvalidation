package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

// Decimal Precision
public class PrecisionScaleValidator extends Validator<TestModel> {
    public PrecisionScaleValidator(int precision, int scale) {
        ruleFor(TestModel::getPrice).precisionScale(precision, scale);
    }
}
