package com.meltmedia.dropwizard.jongo.guice;

import java.util.Set;

import org.jongo.Jongo;
import org.jongo.marshall.jackson.JacksonMapper;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.meltmedia.dropwizard.jongo.AddMixin;
import com.meltmedia.dropwizard.jongo.JongoJodaModule;
import com.meltmedia.dropwizard.mongo.MongoConfiguration;
import com.mongodb.MongoClient;

public class JongoModule extends AbstractModule {
  private static final Logger log = LoggerFactory.getLogger(JongoModule.class);

  @Override
  protected void configure() {
    // nothing to add here, we just need the provider.
  }

  @Provides
  @Singleton
  public Jongo createJongo( MongoConfiguration mongoConfiguration, final MongoClient mongoClient, Reflections reflections, MetricRegistry registry ) {
    registry.register("connections", new Gauge<Integer>() {
      @Override
      public Integer getValue() {
        return mongoClient.getConnector().getDBPortPool(mongoClient.getAddress()).getTotal();
      }
    });

    log.info("building jongo daos");
    JacksonMapper.Builder builder = new JacksonMapper.Builder();
    SimpleModule module = new SimpleModule();
    Set<Class<?>> daos = reflections.getTypesAnnotatedWith(AddMixin.class);
    for( Class<?> dao : daos ) {
      log.info("loading dao " + dao);
      AddMixin jongoDao = dao.getAnnotation(AddMixin.class);
      if( jongoDao != null ) {
        Class<?> type = jongoDao.type();
        Class<?> mixinType = jongoDao.mixin();
        log.info("adding mixin {} to {}", mixinType, type);
        module.setMixInAnnotation(type, mixinType);
      }
      else {
        log.info("dao {} does not have JongoDao annotation", dao);
      }
    }
    builder.registerModule(module);
    builder.registerModule(new JongoJodaModule());

    return new Jongo(mongoClient.getDB(mongoConfiguration.getDatabase()), builder.build());
  }

}
