package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class MatchesNullValidator extends Validator<TestModel> {
    public MatchesNullValidator() {
        ruleFor(TestModel::getValue).matches("^test.*$");
    }
}
