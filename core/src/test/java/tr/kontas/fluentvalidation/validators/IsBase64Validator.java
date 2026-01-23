package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsBase64Validator extends Validator<TestModel> {
    public IsBase64Validator() {
        ruleFor(TestModel::getValue).isBase64();
    }
}
