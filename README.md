# Logical expression evaluator

Implementation of a logical expression evaluator.

## Specification
Application expose two HTTP endpoints:

### API Definition:

```
/expression
```

### API Input:

This API endpoint should take name of the logical expression and its value:

### API Response:

For each request executed against the API endpoint you should return an unique identifier that represents the identifier of logical expression.

### Workflow:

When this API is being called new logical expression should be created and identifier of newly created logical expression is returned.

### Example:

```
Name: Complex logical expression
```
```
Value: (customer.firstName == "JOHN" && customer.salary < 100) OR (customer.address != null && customer.address.city == "Washington")
```

### API Definition:

```
/evaluate
```

### API Input:

This API endpoint takes expression ID and JSON object as input.

### API Output:

Returns the result of evaluation by using the requested expression and provided JSON object.

### Workflow:

When this API is being called requested logical expression should be evaluated using the provied JSON object.

### Example:

```
{
  "customer":
  {
    "firstName": "JOHN",
    "lastName": "DOE", 
    "address":
    {
      "city": "Chicago",
      "zipCode": 1234, 
      "street": "56th", 
      "houseNumber": 2345
    },
    "salary": 99,
    "type": "BUSINESS"
  }
}
```

## Additional Information
Used following frameworks:

### Spring JPA
H2 database running in memory (data will not be persistent across application restarts).

### Running the exercise with maven
```mvn spring-boot:run```