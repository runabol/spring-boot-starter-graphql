# Spring Boot Starter GraphQL

Bringing GraphQL goodness to Spring Boot applications. 

This project is based on the fine work made by the folks behind the [graphql-java](https://github.com/graphql-java/graphql-java) library.

**Note**: To support GraphQL's [subscriptions](http://graphql.org/blog/subscriptions-in-graphql-and-relay/) feature this module depends on Spring 5's WebFlux module and therefore Spring Boot 2.

# GraphQL Primer

In its most basic definition, GraphQL is a **query language** for your API.

Say you have a database of movies and you decide that you want to expose it to app developers through an API. 

One way you could go about it is by using the traditional REST API approach:

Request:

```
GET /movies?q=big
```

Response:

```
[

  {
    "id":"1",
    "title":"Big",
    "synopsis":"After a wish turns 12-year-old Josh Baskin (David Moscow) into a 30-year-old man (Tom Hanks)...",
    "duration":"..",
    "poster":"http://...",
    "cast":[
        {
           "name":"...",
           "role":"..."
        }
    ]
  },
  
  {
    "id":"2",
    "title":"...",
    "synopsis":"...",
    "duration":"..",
    "poster":"http://...",
    "cast":[
        {
           "name":"...",
           "role":"..."
        }
    ]
  }
  
  ...
  
]
``` 

While this approach is totally workable it presents several challenges: 

1. **Waste**. Let's say the app developers is building a screen that gives a movie listing with a thumbnail of the movie. Since he does not need the movie synopsis, cast etc. he is going to throw away a lot of data that had to be collected and tranferred over the wire. That's a lot of waste. One common way that people used to solve this problem is by introducing a `fields` query parameter to their API where you specify the fields you want to get back. This is an okay approach but comes with a significant amount of complexity to implement in the backend and does not scale very well to nested properties.

2. **Lack of standardization**. REST-based APIs are extremely custom from one to the next which means that there is no standard way to interrogate them or explore them. You are at the mercy of the API author to write proper documenations and keep these up to date in order to understand how the API works. 

3. **Evolution**. In REST world, when you need to add new properties to your response you can either take the chance of adding them and hoping nothing breaks or you can add a `/v2/movies` endpoint and add your properties there. This could quickly get out of hand and adds an additional dimension to your API which you would do great without.
  
4. **Taxing the Backend**. When you create the first version of your API it's typically nice and fast. But as you introduce more database joins, more external calls required for your response and more calculations things start slowing down. This impacts not just the new features of your apps that need the new pieces of data but any festures that use the API.

## GraphQL to the rescue  

# Usage

Add the `spring-boot-starter-graphql` dependency to your Spring Boot app:

```
<dependency>
  <groupId>com.creactiviti</groupId>
  <artifactId>spring-boot-starter-graphql</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>

<repositories>
   <repository>
      <id>maven-snapshots</id>
      <url>http://oss.sonatype.org/content/repositories/snapshots</url>
      <layout>default</layout>
      <releases>
         <enabled>false</enabled>
      </releases>
      <snapshots>
         <enabled>true</enabled>
      </snapshots>
   </repository>
</repositories>
```

# Kicking the tires

`spring-boot-starter-graphql` comes with a built-in `ping` query to test that everything is fine:

```
curl -X POST -H "Content-Type:application/json" -d '{"query":"{ping}"}' http://localhost:8080/graphql
```

# Your first GraphQL Query

```
@Component
public class HelloWorld implements QueryBuilder {

  @Override
  public void build (Builder aBuilder) {
    aBuilder.field(Fields.field("hi")
                         .type(Scalars.GraphQLString)
                         .staticValue("Hello World"));
  }

}
```
