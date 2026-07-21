package com.example.clinicmvcspring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.clinicmvcspring.models.AuditAction;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface Audit {
    AuditAction action();
}
