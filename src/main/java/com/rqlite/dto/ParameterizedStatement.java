package com.rqlite.dto;

import com.google.api.client.json.GenericJson;

public class ParameterizedStatement extends GenericJson {
  public String query;

  public Object[] arguments;

  public ParameterizedStatement(String query, Object[] arguments) {
    this.query = query == null ? "" : query;
    this.arguments = arguments == null ? new Object[]{} : arguments;
  }
}
