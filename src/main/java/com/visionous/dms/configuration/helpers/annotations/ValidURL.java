package com.visionous.dms.configuration.helpers.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.visionous.dms.configuration.validators.UrlValidator;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE}) 
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UrlValidator.class)
@Documented
public @interface ValidURL {   
    String message() default "{javax.validation.constraints.URL.message}";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}