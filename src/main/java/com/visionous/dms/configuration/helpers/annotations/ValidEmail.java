/**
 * 
 */
package com.visionous.dms.configuration.helpers.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.visionous.dms.configuration.validators.EmailValidator;

/**
 * @author delimeta
 *
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE}) 
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidEmail {   
    String message() default "jakarta.validation.constraints.Email.message";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}