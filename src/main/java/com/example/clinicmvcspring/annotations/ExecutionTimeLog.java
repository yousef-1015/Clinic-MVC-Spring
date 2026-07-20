package com.example.clinicmvcspring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // this custom annotation should be for methods
@Retention(RetentionPolicy.RUNTIME) // keep at runtime
public @interface ExecutionTimeLog {

}
