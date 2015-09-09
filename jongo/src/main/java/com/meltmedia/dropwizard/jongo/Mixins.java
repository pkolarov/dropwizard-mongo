package com.meltmedia.dropwizard.jongo;

import org.jongo.marshall.jackson.oid.ObjectId;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Common Jackson mixins.
 * 
 * @author Christian Trimble
 *
 */
public class Mixins {
  public static abstract class StringIdMixin {
    @JsonProperty("_id")
    public String id;

    @JsonProperty("_id")
    public abstract String getId();

    @JsonProperty("_id")
    public abstract void setId( String id );
  }
  
  public static abstract class ObjectIdMixin {
    @ObjectId
    @JsonProperty("_id")
    public String id;

    @ObjectId
    @JsonProperty("_id")
    public abstract String getId();

    @ObjectId
    @JsonProperty("_id")
    public abstract void setId( String id );    
  }
}
