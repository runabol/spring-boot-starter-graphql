package com.creactiviti.spring.boot.starter.graphql;

import graphql.schema.GraphQLType;

/**
 * A interface for building {@link GraphQLType} instances
 * that participate in the GraphQL schema. Registered
 * implementation will be invoked on schema creation time
 * and registered with the GraphQL schema.
 * 
 * @author Arik Cohen
 * @since Feb 08, 2018
 */
public interface TypeBuilder {

  /**
   * Build a {@link GraphQLType} instance.
   * 
   * @return The created GraphQL type.
   */
  GraphQLType build ();
  
}
