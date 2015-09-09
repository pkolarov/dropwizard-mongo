package com.meltmedia.dropwizard.jongo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Support for declaring multiple AddMixin annotaitons.
 * 
 * @author Christian Trimble
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface AddMixins {
  AddMixin[] value();
}
