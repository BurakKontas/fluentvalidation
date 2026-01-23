package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class DoesNotContainValidator extends Validator<TestModel> {
    public DoesNotContainValidator(String item) {
        ruleFor(TestModel::getList).doesNotContain(item);
    }
}
