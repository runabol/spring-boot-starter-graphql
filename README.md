# Spring Boot Starter GraphQL

The aim of this project is to get you easily started with GraphQL running in your Spring Boot based apps.

This project is based on the fine work made by the folks behind the [graphql-java](https://github.com/graphql-java/graphql-java) library.

**Note**: To support GraphQL's [subscriptions](http://graphql.org/blog/subscriptions-in-graphql-and-relay/) feature this module depends on Spring 5's WebFlux module and therefore Spring Boot 2.

# GraphQL Primer

In its most basic definition, GraphQL is a **query language** for your API.

Say you have a database of movies and you decide that you want to expose it to app developers through an API. 

One way you could go about it is by using the traditional REST API approach:

```
GET /movies?q=big
 
[

  {
    "id":"1",
    "title":"Big",
    "synopsis":"After a wish turns 12-year-old Josh Baskin into a 30-year-old man...",
    "duration":"1h44m",
    "images":[{
       "type":"thumbnail",
       "url":"http://...",
       "resolution":"150x200"
    },
    {
       "type":"full",
       "url":"http://...",
       "resolution":"1920x1080"
    }],
    "genres":[...],
    "cast":[
        {
           "name":"Tom Hanks",
           "role":"..."
        }
    ]
  },
  
  {
    "id":"2",
    "title":"The Big Lebowski",
    "synopsis":"Jeff Bridges plays Jeff Lebowski who insists on being called "the Dude,"...",
    "duration":"1h57m",
    "images":[{
       "type":"thumbnail",
       "url":"http://...",
       "resolution":"150x200"
    },
    {
       "type":"full",
       "url":"http://...",
       "resolution":"1920x1080"
    }],
    "genres":[...],
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

1. **Waste**. Let's say the app developers is building a screen that gives a movie listing with a thumbnail of the movie. Since the app developer does not need the movie synopsis, cast, genres etc. they are going to throw away a lot of data that had to be collected and tranferred over the wire. That's a lot of waste. One common way that people used to solve this problem is by introducing a `fields` query parameter to their API where you specify the fields you want to get back. This is an okay approach but comes with a significant amount of complexity to implement in the backend and does not scale very well to nested properties.

2. **Lack of standardization**. REST-based APIs are extremely custom from one to the next which means that there is no standard way to interrogate them or explore them. You are at the mercy of the API author to write proper documenations and keep these up to date in order to understand how the API works. 

3. **Evolution**. In REST world, when you need to add new properties to your response you can either take the chance of adding them and hoping nothing breaks or you can add a `/v2/movies` endpoint and add your properties there. This could quickly get out of hand and adds an additional dimension to your API which you would do great without.
  
4. **Taxing the Backend**. When you create the first version of your API it's typically nice and fast. But as you introduce more database joins, more external calls required for your response and more calculations things start slowing down. This impacts not just the new features of your apps that need the new pieces of data but any festures that use the API.

## GraphQL to the rescue  

Alternatively, using GraphQL we expose a single endpoint (typically `/graphql`) which accepts a GraphQL query from the client and responds with the requested data. Here's an example: 

```
POST /graphql

{
   getAllMovies {
      id        // this is a field
      title,
      image(type:"thumbnail") {  // fields can be nested
         url
      }
   }
}
```

```
[

  {
    "id":"1",
    "title":"Big",
    "image:{
      "url":"http://..."
    }
  },
  
  {
    "id":"2",
    "title":"The Big Lebowski",
    "image:{
      "url":"http://..."
    }
  }
]
```

What just happened? 

1. **Waste**. Since we only want to get the movie `id`, `title` and the thumbnail URL for each movie, that's exactly what we are getting back. The most of collecting and transferring the data is completely eliminated.

2. **Lack of standardization**. The GraphQL [spec](http://facebook.github.io/graphql/October2016/) provides a commong ground for agreement between API authors and client consumers. This opens the door for tools such as [GraphiQL](https://github.com/graphql/graphiql) and [Voyager](https://github.com/APIs-guru/graphql-voyager) to interrogate GraphQL-based APIs.

3. **Evolution**. Since GraphQL clients must *explicitly* request the pieces of information that they want to get back, there is no worry in adding new properties to the API and breaking older clients. 

4. **Taxing the backend**. Since the client is only asking for exactly what it needs, the backend it free to only perform the necessary calculations and data retrieval operations necessary to fullfill the request. 

GraphQL has many more advantages and features but I hope that this gives you a good sense of what's possible with GraphQL.

For more information check out the offical [GraphQL](http://graphql.org/) website.

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
$ curl -s -X POST -H "Content-Type:application/json" -d '{"query":"{ ping }"}' http://localhost:8080/graphql

{
  "data": {
    "ping": "OK"
  },
  "errors": [],
  "extensions": null
}
```

# Your first GraphQL Query

```
@Component
public class HelloWorld implements QueryBuilder {

  @Override
  public void build (Builder aBuilder) {
    aBuilder.field(Fields.field("hello")  // queries are just fields on the schema's built-in Query type.
                         .type(Scalars.GraphQLString)
                         .staticValue("Hi there!"));
  }

}
```

```
curl -s -X POST -H "Content-Type:application/json" -d '{"query":"{ hello }"}' http://localhost:8080/graphql

{
  "data": {
    "hello": "Hi there!"
  },
  "errors": [],
  "extensions": null
}
```

# Your second GraphQL query

OK, let's fetch some data now to make this more interesting:

```
@Component
public class GetAllMoviesQuery implements QueryBuilder {

  @Override
  public void build (Builder aBuilder) {
    aBuilder.field(Fields.field("getAllMovies")
                         .type(Types.list(Movie.REF)) // query result type
                         .argument(Arguments.stringArgument("q")) // query arguments
                         .dataFetcher((env) -> {
                           
                           // in the real-world you would probably 
                           // fetch the data from a database but 
                           // i'm keeping it simple for the example. 
                           // The point is that the data can come from 
                           // anywhere: database, another service, 
                           // calculated on the fly, etc.
                           
                           Map<String, Object> movie = new HashMap<>();
                           
                           movie.put("title", "Big");
                           movie.put("synopsis", "After a wish turns 12-year-old Josh Baskin into a 30-year-old man...");
                           movie.put("duration", "1h44m");
                           
                           return Arrays.asList(movie);
                         }));
  }

}
```

```
@Component
public class Movie implements TypeBuilder {
  
  public static final String NAME = "Movie";
  public static final GraphQLTypeReference REF = Types.ref(NAME);

  @Override
  public GraphQLType build () {
    return Types.objectTypeBuilder()
                .name(NAME)
                .field(Fields.notNull(Fields.stringField("id")))
                .field(Fields.stringField("title"))
                .field(Fields.stringField("synopsis"))
                .field(Fields.stringField("duration"))
                .field(Fields.field("image")  
                             .argument(Arguments.stringArgument("type"))
                             .type(Image.REF)
                             .dataFetcher((env) -> {
                                
                                // fetch the right image.
                               
                               // will NOT be executed if the user
                               // did not ask for the image field!
                                 
                             })
                )
                .build();
  }

}
```

```
$ curl -s -X POST -H "Content-Type:application/json" -d '{"query":"{ getAllMovies { title synopsis } }"}' http://localhost:8080/graphql

{
  "data": {
    "getAllMovies": [
      {
        "title": "Big",
        "synopsis": "After a wish turns 12-year-old Josh Baskin into a 30-year-old man..."
      }
    ]
  },
  "errors": [],
  "extensions": null
}
```

# Mutations

So far we looked at reading data from the API. Mutations are GraphQL's approach to writing data.

```
@Component
public class AddMovie implements MutationBuilder {

  @Override
  public void build (Builder aBuilder) {
    aBuilder.field(Fields.field("addMovie")
                         .argument(Arguments.notNull(Arguments.stringArgument("title")))
                         .argument(Arguments.stringArgument("synopsis"))
                         .argument(Arguments.stringArgument("duration"))
                         .type(Movie.REF)
                         .dataFetcher((env) -> {
                           Map<String,Object> record = new HashMap<>();
                           
                           record.put("id", UUID.randomUUID().toString().replace("-",""));
                           record.put("title", env.getArgument("title"));
                           record.put("synopsis", env.getArgument("synopsis"));
                           record.put("duration", env.getArgument("duration"));
                           
                           // save the the database
                           
                           return record;
                         }));
  }

}
```

```
$ curl -s -X POST -H "Content-Type:application/json" -d '{"query":"mutation { addMovie (title:\"Big Fish\") { id title } }"}' http://localhost:8080/graphql

{
  "data": {
    "addMovie": {
      "id": "8294094ebb034c7f82ef1170b95f69b0",
      "title": "Big Fish"
    }
  },
  "errors": [],
  "extensions": null
}
```



# Subscriptions

Both queries and mutations make use of the traditional request/response model: the client makes a request with the specifics of what it wants to get back and the server responds appropriately. 

Subscriptions on the other hand, are publish/subsribe based mechanism: the server published some sort of notifications that clients can subscribe to. 

For example we can let clients know whenever a movie was added:

```
@Component
public class MovieAddedSubscription implements SubscriptionBuilder {
  
  private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  
  @Override
  public void build (Builder aBuilder) {
    aBuilder.field(Fields.field("movieAdded")
                         .type(Movie.REF)
                         .dataFetcher((env)->Flux.create ((emitter) -> {
                           
                           // use a scheduled executor to simulate an 
                           // event happening on another thread event 
                           // second.
                           
                           executor.scheduleAtFixedRate(() -> onMovieAdded(emitter), 1, 1, TimeUnit.SECONDS);
                           
                         })));
  }
  
  private void onMovieAdded (FluxSink<Object> aEmitter) {
    Map<String, String> movie = new HashMap<>();
    movie.put("id", UUID.randomUUID().toString().replace("-",""));
    movie.put("title", "...");
    aEmitter.next(movie);
  }

}
```

```
$ curl -s -X POST -H "Content-Type:application/json" -H "Accept:text/event-stream" -d '{"query":"subscription { movieAdded { id title } }"}' http://localhost:8080/graphql

data:{"data":{"id":"ef006c125e61435d965bbd9492d00990","title":"..."},"errors":[],"extensions":null}

data:{"data":{"id":"160ac99f81f740589b6062f5cb70915c","title":"..."},"errors":[],"extensions":null}

... and on every second

```  

**Note:** Make sure to use the `Accept:text/event-stream` when subscribing.

# License

Version 2.0 of the Apache License.