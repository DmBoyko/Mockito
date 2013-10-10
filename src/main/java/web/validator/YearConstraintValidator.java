package web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class YearConstraintValidator implements ConstraintValidator<Year, String> {

    @Override
    public void initialize(Year year) {
    }

    @Override
    public boolean isValid(String target, ConstraintValidatorContext cxt) {
        if (target == null) {
            return true;
        }
        return target.matches("(([012]\\d)|3[01])-((0\\d)|(1[012]))-\\d{4}");
    }

}