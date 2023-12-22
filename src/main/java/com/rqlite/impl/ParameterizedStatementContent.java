package com.rqlite.impl;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.api.client.http.AbstractHttpContent;
import com.rqlite.dto.ParamaterizedStatement;

public class ParameterizedStatementContent extends AbstractHttpContent {

  private final ParamaterizedStatement[] stmts;
  private static final JsonFactory JSON_FACTORY = new JsonFactory();

  protected ParameterizedStatementContent(ParamaterizedStatement[] stmts) {
    super("application/json");
    if (stmts == null) {
      stmts = new ParamaterizedStatement[]{};
    }
    this.stmts = stmts;
  }

  @Override
  public void writeTo(OutputStream out) throws IOException {

    JsonGenerator json = JSON_FACTORY.createGenerator(out);
    json.writeStartArray();
    for (ParamaterizedStatement s : stmts) {
      json.writeStartArray();
      json.writeString(s.query);
      if (s.arguments != null) {
        for (Object arg: s.arguments) {
          json.writeObject(arg);
        }
      }
      json.writeEndArray();
    }
    json.writeEndArray();
    json.close();
  }

}
