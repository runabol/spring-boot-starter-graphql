package com.creactiviti.spring.boot.starter.graphql;

import graphql.Scalars;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLNonNull;

/**
 * This class consists exclusively of static methods that operate on or return
 * GraphQL field arguments.
 * 
 * @author Arik Cohen
 * @since Feb 10, 2018
 */
public final class Arguments {
  
  private Arguments () {}

  public static GraphQLArgument.Builder stringArgument (String aName) {
    return GraphQLArgument.newArgument()
                          .name(aName)
                          .type(Scalars.GraphQLString);
  }
  
  public static GraphQLArgument.Builder notNull (GraphQLArgument.Builder aBuilder) {
    GraphQLArgument arg = aBuilder.build();
    return GraphQLArgument.newArgument()
                          .name(arg.getName())
                          .defaultValue(arg.getDefaultValue())
                          .definition(arg.getDefinition())
                          .description(arg.getDescription())
                          .type(new GraphQLNonNull(arg.getType()));
  }
  
}
