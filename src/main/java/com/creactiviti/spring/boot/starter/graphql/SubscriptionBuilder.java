package com.creactiviti.spring.boot.starter.graphql;

import graphql.schema.GraphQLObjectType.Builder;

/**
 * An interface for building the Subscription type of the GraphQL schema. 
 * 
 * @author Arik Cohen
 * @since Feb 10, 2018
 */
public interface SubscriptionBuilder {

  /**
   * Invoked on GraphQL Schema creation time so as to contribute to building the Subscription type.
   * 
   * @param aBuilder
   *           The Subscription type builder.
   */
  void build (Builder aBuilder);
  
}
