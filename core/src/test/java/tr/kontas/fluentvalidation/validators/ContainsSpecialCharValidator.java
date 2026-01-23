package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ContainsSpecialCharValidator extends Validator<TestModel> {
    public ContainsSpecialCharValidator() {
        ruleFor(TestModel::getValue).containsSpecialChar();
    }
}
