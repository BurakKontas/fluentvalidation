package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ContainsValidator extends Validator<TestModel> {
    public ContainsValidator(String item) {
        ruleFor(TestModel::getList).contains(item);
    }
}
