package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ContainsCollectionNullValidator extends Validator<TestModel> {
    public ContainsCollectionNullValidator() {
        ruleFor(TestModel::getList).contains("test");
    }
}
