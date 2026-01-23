package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class HasUniqueItemsValidator extends Validator<TestModel> {
    public HasUniqueItemsValidator() {
        ruleFor(TestModel::getList).hasUniqueItems();
    }
}
