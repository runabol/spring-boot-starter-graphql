package com.creactiviti.spring.boot.starter.graphql;

import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldDefinition.Builder;
import graphql.schema.GraphQLNonNull;

/**
 * This class consists exclusively of static methods that operate on or return
 * GraphQL fields.
 * 
 * @author Arik Cohen
 * @since Feb 10, 2018
 */
public final class Fields {
  
  private Fields () {}

  public static Builder stringField (String aName) {
    return create(aName).type(Scalars.GraphQLString);
  }
  
  public static Builder spelField (String aName, String aExpression) {
    return create(aName).dataFetcher(new SpelDataFetcher(aExpression));
  }
  
  public static Builder notNull (Builder aSource) {
    GraphQLFieldDefinition field = aSource.build();
    return create(field.getName()).argument(field.getArguments())
                                  .type(new GraphQLNonNull(field.getType()))
                                  .dataFetcher(field.getDataFetcher())
                                  .definition(field.getDefinition())
                                  .deprecate(field.getDeprecationReason())
                                  .description(field.getDescription());
  }
  
  public static Builder field (String aName) {
    return create(aName);
  }
  
  private static Builder create (String aName) {
    return new Builder().name(aName);
  }
  
}
