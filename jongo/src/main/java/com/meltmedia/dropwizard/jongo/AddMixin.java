package com.meltmedia.dropwizard.jongo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a mixin for a type.  Jackson only supports using one mixin per a type at a time.
 * 
 * @author Christian Trimble
 */
@Repeatable(AddMixins.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface AddMixin {

  Class<?> mixin();
  Class<?> type();

}
