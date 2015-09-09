package com.meltmedia.dropwizard.jongo.junit;

import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.validation.valuehandling.OptionalValidatedValueUnwrapper;

import org.hibernate.validator.HibernateValidator;
import org.jongo.Jongo;
import org.jongo.Mapper;
import org.jongo.marshall.jackson.JacksonMapper;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;

import com.meltmedia.dropwizard.jongo.junit.JongoRule;
import com.meltmedia.dropwizard.mongo.junit.MongoRule;

import javax.validation.Validation;

/**
 * A base class for testing with Jongo.  The dao type passed to this class must define a contructor that
 * takes a single argument of Jongo.
 * 
 * @author Christian Trimble
 *
 * @param <D> the type of the DAO.  Must provide a single argument contructor that takes an instance of Jongo.
 */
public abstract class BaseDaoIT<D> {
  @ClassRule
  public static MongoRule mongoRule = new MongoRule("localhost", 27017);

  @Rule
  public JongoRule jongoRule;

  protected Class<D> daoType;
  protected D dao;

  public BaseDaoIT( Class<D> daoType, String dbName ) {
    this.daoType = daoType;
    jongoRule = jongoRule.builder()
      .withMongoClient(mongoRule::getClient)
      .withDBName(dbName)
      .build();
  }

  /**
   * Initializes the main dao for this test.
   * 
   * @throws Exception
   */
  @Before
  public final void initDao() throws Exception {
    dao = initializeDao(daoType);
  }

  /**
   * Returns the current instance of Jongo, from the jongo rule.
   * 
   * @return the current instance of Jongo.
   */
  protected Jongo getJongo() {
    return jongoRule.getJongo();
  }

  /**
   * Initializes the specified dao class.  The class must provide a single argument contructor that takes an instance of Jongo.
   * 
   * @param daoClass the type of the dao.
   * @return the instance of the dao
   * @throws Exception if there were any problems encountered when creating the dao.
   */
  protected <T> T initializeDao( Class<T> daoClass ) throws Exception {
    return daoClass.getConstructor(Jongo.class).newInstance(getJongo());
  }

  protected Mapper createMapper() {
    return new JacksonMapper.Builder().build();
  }

  /**
   * This will create a ConfigurationFactory for the given Class.
   *
   * @param configurationClass
   * @param <T>
   * @return
   */
  public static <T> ConfigurationFactory<T> getConfigFactory( Class<T> configurationClass ) {
    return new ConfigurationFactory<T>(
      configurationClass,
      Validation
        .byProvider(HibernateValidator.class)
        .configure()
        .addValidatedValueHandler(new OptionalValidatedValueUnwrapper())
        .buildValidatorFactory()
        .getValidator(),
      Jackson.newObjectMapper(),
      "dw");
  }
}
