package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsSnakeCaseValidator extends Validator<TestModel> {
    public IsSnakeCaseValidator() {
        ruleFor(TestModel::getValue).isSnakeCase();
    }
}
