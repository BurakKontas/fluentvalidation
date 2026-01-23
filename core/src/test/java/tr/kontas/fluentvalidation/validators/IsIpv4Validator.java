package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsIpv4Validator extends Validator<TestModel> {
    public IsIpv4Validator() {
        ruleFor(TestModel::getValue).isIpv4();
    }
}
