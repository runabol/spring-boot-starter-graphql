package com.creactiviti.spring.boot.starter.graphql;

import org.springframework.stereotype.Component;

import graphql.schema.GraphQLObjectType.Builder;

/**
 * @author Arik Cohen
 * @since Feb 10, 2018
 */
@Component
public class Ping implements QueryBuilder {

  @Override
  public void build (Builder aBuilder) {
    aBuilder.field(Fields.stringField("ping").staticValue("OK"));
  }

}
