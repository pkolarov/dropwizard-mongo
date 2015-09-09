package com.meltmedia.dropwizard.jongo.junit;

import java.util.function.Consumer;

import org.jongo.marshall.jackson.JacksonMapper;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.fasterxml.jackson.core.type.TypeReference;

public class JongoDaoRule<T>
  implements TestRule {
  
  public static class Builder {
    //public <T, K, D extends JongoDao<T, K>> Builder withType( TypeReference<T> reference ) {
      
    //}
  }

  @Override
  public Statement apply( Statement base, Description description ) {
    // TODO Auto-generated method stub
    return null;
  }
  
  public Consumer<JacksonMapper.Builder> configurator() {
    return (builder)->{
     
    };
  }
}
