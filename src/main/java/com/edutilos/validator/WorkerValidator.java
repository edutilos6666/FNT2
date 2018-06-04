package com.edutilos.validator;

import com.edutilos.model.Worker;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by edutilos on 03.06.18.
 */
@Component
public class WorkerValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Worker.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Worker worker = (Worker)target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "worker.id.empty.or.ws" );
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "worker.name.empty.or.ws");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "age", "worker.age.empty.or.ws");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "wage", "worker.wage.empty.or.ws");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "active", "worker.active.empty.or.ws");
        if(worker.getId() <= 0) {
            errors.rejectValue("id", "worker.id.non_positive");
        }
        if(worker.getId() >= 100) {
            errors.rejectValue("id", "worker.id.too_large");
        }
        if(worker.getAge() <= 10)  {
            errors.rejectValue("age", "worker.age.too_young");
        }
        if(worker.getAge() >= 100) {
            errors.rejectValue("age", "worker.age.too_old");
        }

    }
}
