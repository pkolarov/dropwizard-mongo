package com.meltmedia.dropwizard.jongo;

import java.util.Optional;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.DBRef;

/**
 * A base DAO implementation for Jongo.  Subclasses should provide a single constructor that takes an instance of Jongo, marked with {@literal @}Inject.
 * 
 * Implementations should look something like this:
 * 
 * ```java
 * public class TypeDao<Type, ObjectId> 
 *   extends BasicDao<Type, ObjectId> {
 *   
 *   {@literal @}Inject
 *   public TypeDao( Jongo jongo ) {
 *     super( jongo, Type.class, "type");
 *   }
 *   
 *   // find, update, etc. methods go here.
 * }
 * ```
 * 
 * @author Christian Trimble
 *
 * @param <T> the entity type
 * @param <K> the id type
 */
public abstract class BasicDao<T, K> {
  public Class<T> entityType;
  public Class<K> keyType;
  protected Jongo jongo;
  protected MongoCollection collection;
  protected String collectionName;

  protected BasicDao( Jongo jongo, Class<T> entityType, String collectionName ) {
    this.jongo = jongo;
    this.collectionName = collectionName;
    this.collection = jongo.getCollection(collectionName);
    this.entityType = entityType;
  }

  public MongoCollection getCollection() {
    return collection;
  }
  
  public DBRef createRef( String id ) {
    return new com.mongodb.DBRef(jongo.getDatabase(), collectionName, id);
  }

  public Optional<T> findById( K key ) {
    return findById(key, entityType);
  }

  public <U> Optional<U> findById( K key, Class<U> type ) {
    return Optional.ofNullable(getCollection()
      .findOne("{_id: #}", key)
      .as(type));
  }

  public Iterable<T> findPage( Pagination pagination ) {
    return getCollection()
      .find()
      .skip(pagination.getOffset())
      .limit(pagination.getLimit())
      .as(entityType);
  }

  public Iterable<T> findPage( int page, int pageSize ) {
    return findPage(page, pageSize, entityType);
  }

  public <T> Iterable<T> findPage( int page, int pageSize, Class<T> type ) {
    return getCollection().find().skip(page * pageSize).limit(pageSize).as(type);
  }

  public void save( T t ) {
    getCollection().save(t);
  }
}
