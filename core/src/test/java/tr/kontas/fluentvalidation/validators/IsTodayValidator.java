package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class IsTodayValidator extends Validator<TestModel> {
    public IsTodayValidator() {
        ruleFor(TestModel::getBirthDate).isToday();
    }
}
