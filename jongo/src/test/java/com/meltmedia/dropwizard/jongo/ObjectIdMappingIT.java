package com.meltmedia.dropwizard.jongo;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.meltmedia.dropwizard.jongo.junit.JongoRule;
import com.meltmedia.dropwizard.mongo.junit.MongoRule;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Ensure that when working with this project, one can map string ids to/from mongo
 * object ids.
 * 
 * @author Christian Trimble
 *
 */
public class ObjectIdMappingIT {
  @ClassRule public static MongoRule mongoRule = new MongoRule("localhost", 27017);
  
  public abstract class ObjectIdOnly {
    @org.jongo.marshall.jackson.oid.ObjectId
    private String _id;
  }
  
  @Rule public JongoRule jongoRule = JongoRule.builder()
    .withDBName("jongo_id_test")
    .withMongoClient(mongoRule::getClient)
    .addSimpleModule((module)->{
      module.setMixInAnnotation(Entry.class, Mixins.ObjectIdMixin.class);
      module.setMixInAnnotation(UnderscoreId.class, ObjectIdOnly.class);
    })
    .build();
  
  BasicDao<UnderscoreId, String> dao;
  BasicDao<Entry, String> entityDao;
  
  @Before
  public void setUp() {
    dao = new BasicDao<>(jongoRule.getJongo(), UnderscoreId.class, UnderscoreId::getId, UnderscoreId::setId, "underscore_id");
    entityDao = new BasicDao<>(jongoRule.getJongo(), Entry.class, Entry::getId, Entry::setId, "entry_id");
  }

  @Test
  public void mapsFieldWithDefaultName() {
    String id = objectIdString();
    UnderscoreId template = new UnderscoreId().withId(id).withAdditionalProperty("data", "data");
    UnderscoreId result = dao.upsertById(template);
    assertThat(result, equalTo(template));
  }
  
  @Test
  public void mapsFieldWithDifferentName() {
    String id = objectIdString();
    Entry template = new Entry().withId(id).withAdditionalProperty("data", "data");
    Entry result = entityDao.upsertById(template);
    assertThat(result, equalTo(template));
  }
  
  public static String objectIdString() {
    return new ObjectId().toString();
  }
}
