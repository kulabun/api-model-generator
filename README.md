# api-model-generator
Project presents a set of annotations and annotation processor to generate api-model-entities from your persistence entities.
Generated models are truncated versions of your persistence entities used to limit input from REST API clients.

# Example
Lets imagine we have the following type. We want to use model-model(symmetric request/responses model) in out REST API, 
but we want to make some fields read-only. One of approach is simply ignore it in your code, but it means that you have 
to always keep in your mind what field you could change and which not. Better approach is to use different types for your
request models and persistence entities. Creating such types and supporting them throughout the life of the project can be quite costly. 
It will be better if the request types would update automatically on your persistence type update. 

```java
@GenerateApiModel
public class Person {
    private UUID id;

    @ApiField
    private String firstName;

    @ApiField
    private String lastName;
    
    private String password;
    
    @ApiField(type = ApiField.Type.Reference, referenceModel = @ApiField.Model(fields = "id"))
    private Company company;
    
    private LocalDateTime created;
    
    ...
}
```

Generated request entity
```java
public class PersonModel {
    private String firstName;
    private String lastName;
    private CompanyModel company;
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
        
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public CompanyModel getCompany() {
        return company;
    }
            
    public void setCompany(CompanyModel company) {
        this.company = company;
    }
    
    public static class CompanyModel {
        private String id;
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
    }
}
```

# Release Notes

1.0.0 - rethinking use cases && fully rewrited on javapoem instead of velocity template compiling. 

0.3.0 - Add entity link(projection) support

0.2.0 - Add generics support

0.1.0 - initial version
