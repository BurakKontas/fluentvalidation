package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class ContainsNoSqlInjectionValidator extends Validator<TestModel> {
    public ContainsNoSqlInjectionValidator() {
        ruleFor(TestModel::getValue).containsNoSqlInjection();
    }
}
