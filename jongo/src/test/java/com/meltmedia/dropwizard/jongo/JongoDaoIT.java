package com.meltmedia.dropwizard.jongo;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.meltmedia.dropwizard.jongo.junit.JongoRule;
import com.meltmedia.dropwizard.mongo.junit.MongoRule;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class JongoDaoIT {
  @ClassRule public static MongoRule mongoRule = new MongoRule("localhost", 27017);
  
  @Rule public JongoRule jongoRule = JongoRule.builder()
    .withDBName("jongo_dao_test")
    .withMongoClient(mongoRule::getClient)
    .addConfigurator((builder)->{
      builder.registerModule(new SimpleModule() {{
        this.setMixInAnnotation(Entry.class, Mixins.ObjectIdMixin.class);
      }});
    })
    .build();
  
  @Test
  public void shouldStoreWithObjectId() {
    MongoCollection entryCollection = jongoRule.getJongo().getCollection("entry_test");
    ObjectId id = new ObjectId();
    Entry expected = new Entry().withId(id.toString());
    
    entryCollection.insert(expected);
    Entry actual = entryCollection.findOne("{_id: #}", id).as(Entry.class);
    
    assertThat(expected, equalTo(actual));
  }
  
}
