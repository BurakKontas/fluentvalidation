package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.enums.CascadeMode;
import tr.kontas.fluentvalidation.validation.Validator;

public class CascadeContinueValidator extends Validator<TestModel> {
    public CascadeContinueValidator() {
        ruleFor(TestModel::getValue)
                .cascade(CascadeMode.CONTINUE)
                .notNull()
                .notEmpty()
                .notBlank();
    }
}
