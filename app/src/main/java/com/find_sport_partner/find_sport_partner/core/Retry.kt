package com.urmomenz.urmomenz.core

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * @author dtodt
 */
@Documented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(RetentionPolicy.RUNTIME)
annotation class Retry(
    /**
     * The max retry attempt (default is 3).
     */
    val max: Int = 3
)