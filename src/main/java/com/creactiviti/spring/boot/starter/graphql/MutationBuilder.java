package com.creactiviti.spring.boot.starter.graphql;

import graphql.schema.GraphQLObjectType.Builder;

/**
 * An interface for building the Mutation (Write) portion of a GraphQL schema. 
 * 
 * @author Arik Cohen
 * @since Feb 08, 2018
 */
public interface MutationBuilder {

  /**
   * Invoked on GraphQL Schema creation time so as to contribute to building the Mutation.
   * 
   * @param aBuilder
   *           The Mutation builder object used for generating the Mutation query.
   */
  void build (Builder aBuilder);
  
}
