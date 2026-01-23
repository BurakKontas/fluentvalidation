package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestEnum;
import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsInEnumValidator extends Validator<TestModel> {
    public IsInEnumValidator() {
        ruleFor(TestModel::getValue).isInEnum(TestEnum.class);
    }
}
