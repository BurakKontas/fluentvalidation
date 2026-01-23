package tr.kontas.fluentvalidation.validators;

import tr.kontas.fluentvalidation.TestModel;
import tr.kontas.fluentvalidation.validation.Validator;

public class MapHasMinCountValidator extends Validator<TestModel> {
    public MapHasMinCountValidator(int min) {
        ruleFor(TestModel::getMap).hasMinCount(min);
    }
}

