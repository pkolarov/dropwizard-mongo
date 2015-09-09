package com.meltmedia.dropwizard.jongo;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class Pagination {
  private int offset = 0;
  private int limit = 100;

  public Pagination( @QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("limit") @DefaultValue("100") int limit ) {
    this.offset = offset;
    this.limit = limit;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset( int offset ) {
    this.offset = offset;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit( int limit ) {
    this.limit = limit;
  }
}