package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.enums.CascadeMode;
import tr.kontas.fluentvalidation.validation.Validator;

public class CascadeStopValidator extends Validator<TestModel> {
    public CascadeStopValidator() {
        ruleFor(TestModel::getValue)
                .cascade(CascadeMode.STOP)
                .notNull()
                .notEmpty()
                .notBlank();
    }
}
