package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ContainsPatternNullValidator extends Validator<TestModel> {
    public ContainsPatternNullValidator() {
        ruleFor(TestModel::getValue).containsPattern("test");
    }
}
