package com.meltmedia.dropwizard.jongo;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class Pagination<K> {
  private K lastId;
  private int pageSize = 100;

  public Pagination( @QueryParam("lastId") K lastId, @QueryParam("pageSize") @DefaultValue("100") int pageSize ) {
    this.lastId = lastId;
    this.pageSize = pageSize;
  }

  public K getLastId() {
    return lastId;
  }

  public void setLastId( K lastId ) {
    this.lastId = lastId;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize( int pageSize ) {
    this.pageSize = pageSize;
  }
}