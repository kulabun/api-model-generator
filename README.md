# Request-Entity
Project presents a set of annotations and annotation processor to generate request-entities from your persistence entities.
Generated Request Entities are truncated versions of your persistence entities used to limit input from REST API clients.

# Example
Lets imagine we have the following type. We want to use model-model(symmetric request/responses model) in out REST API, 
but we want to make some fields read-only. One of approach is simply ignore it in your code, but it means that you have 
to always keep in your mind what field you could change and which not. Better approach is to use different types for your
request and persistence entities. Creating such types and supporting them throughout the life of the project can be quite costly. 
It will be better if the request types would update automatically on your persistence type update. 

```java
@RequestEntity
public class Person {
    private UUID id;

    @RequestField
    private String firstName;

    @RequestField
    private String lastName;
    
    private String password;
    
    @RequestField
    private Person parent;

    private LocalDateTime ;
    
    ...
}
```

Generated request entity
```java
public class PersonRequest {
    private String firstName;
    private String lastName;
    private PersonRequest parent;

    ...
}
```

# Release Notes

0.2.0 - Add generics support

0.1.0 - initial version
