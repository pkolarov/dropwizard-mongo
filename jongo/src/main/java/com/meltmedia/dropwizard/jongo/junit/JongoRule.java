package com.meltmedia.dropwizard.jongo.junit;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jongo.Jongo;
import org.jongo.Mapper;
import org.jongo.marshall.jackson.JacksonMapper;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;

/**
 * This rule will drop the specified collection in Mongo before executing, DO NOT USE AGAINST PRODUCTION COLLECTIONS.
 * 
 * A rule for starting Jongo in tests. This test is designed to be used with the @Rule
 * annotation and in conjunction with the MongoRule.
 * 
 * @author Christian Trimble
 * 
 */
public class JongoRule
  implements TestRule
{
  public static class Builder {
    private Supplier<MongoClient> mongoClient;
    private String dbName;
    private List<Consumer<JacksonMapper.Builder>> configurators = Lists.newArrayList(); 

    public Builder withMongoClient(Supplier<MongoClient> mongoClient) {
      this.mongoClient = mongoClient;
      return this;
    }
    
    public Builder withDBName( String dbName ) {
      this.dbName = dbName;
      return this;
    }
    
    public Builder addConfigurator( Consumer<JacksonMapper.Builder> configurator ) {
      configurators.add(configurator);
      return this;
    }
    
    public JongoRule build() {
      return new JongoRule( mongoClient, dbName, configurators );
    }
  }
  
  public static Builder builder() {
    return new Builder();
  }

  protected Supplier<MongoClient> mongoClient;
  protected String dbName;
  protected List<Consumer<JacksonMapper.Builder>> configurators;
  protected Jongo jongo;  

  public JongoRule( Supplier<MongoClient> mongoClient, String dbName, List<Consumer<JacksonMapper.Builder>> configurators ) {
    this.mongoClient = mongoClient;
    this.dbName = dbName;
    this.configurators = configurators;
  }

  /**
   * When this rule is active, returns the Jongo instance.
   */
  public Jongo getJongo() {
    return jongo;
  }

  @Override
  public Statement apply( final Statement statement, final Description description ) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        mongoClient.get().dropDatabase(dbName);

        jongo = new Jongo(mongoClient.get().getDB(dbName), createMapper(JacksonMapper.Builder::new, configurators));

        statement.evaluate();

        jongo = null;
      }

    };
  }

  /**
   * Takes a list of configurator functions and executes them, in order, on a builder.
  */
  private static Mapper createMapper(Supplier<JacksonMapper.Builder> builderSupplier, List<Consumer<JacksonMapper.Builder>> configurators) throws Throwable {
    JacksonMapper.Builder builder = builderSupplier.get();
    configurators.forEach(mapperBuilder->mapperBuilder.accept(builder));
    return builder.build();
  }
}
