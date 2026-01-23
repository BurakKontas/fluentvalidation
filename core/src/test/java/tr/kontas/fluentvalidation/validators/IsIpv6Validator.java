package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsIpv6Validator extends Validator<TestModel> {
    public IsIpv6Validator() {
        ruleFor(TestModel::getValue).isIpv6();
    }
}
