package com.creactiviti.spring.boot.starter.graphql;

import graphql.schema.GraphQLObjectType.Builder;

/**
 * An interface for building the Query (Read) portion  of a GraphQL schema. 
 * 
 * @author Arik Cohen
 * @since Feb 08, 2018
 */
public interface QueryBuilder {

  /**
   * Invoked on GraphQL Schema creation time so as to contribute to building the Query.
   * 
   * @param aBuilder
   *           The Query builder object used for generating the query.
   */
  void build (Builder aBuilder);
  
}
