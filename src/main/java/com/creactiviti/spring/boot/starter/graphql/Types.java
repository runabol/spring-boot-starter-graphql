package com.creactiviti.spring.boot.starter.graphql;

import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLType;
import graphql.schema.GraphQLTypeReference;

/**
 * This class consists exclusively of static methods that operate on or return
 * GraphQL types.
 * 
 * @author Arik Cohen
 * @since Feb 10, 2018
 */
public final class Types {
  
  private Types ()  {}
  
  public static GraphQLInterfaceType.Builder interfaceTypeBuilder () {
    return new GraphQLInterfaceType.Builder ();
  }
  
  public static GraphQLObjectType.Builder objectTypeBuilder () {
    return new GraphQLObjectType.Builder();
  }
  
  public static GraphQLTypeReference ref (String aName) {
    return new GraphQLTypeReference(aName);
  }
  
  public static GraphQLList list (GraphQLType aWrappedType) {
    return new GraphQLList(aWrappedType);
  }
  
}
