# client-service


# Technologies Used
* Java 1.8 openjdk 1.8.0_181
* Spring Security
* Spring Boot API
* H2 Lite Database

# Run

# Swagger API
http://localhost:8082/swagger-ui/index.html

# Rest Standards
* Respecting the Rest service standards

# Design aspects
* SOLID principles
* KISS principle
* HATEOAS
* Use HAL - Hypertext Application Language
* DTO layer
* Repository pattern
* API versioning

# Validation
* For the incoming request are validated using the Spring Boot basic validation framework.
* Email is validated using a regex pattern and error message is sent if it does not stick to standards.
* Custom Validators can be added in case of further level validation.

# Security
* We can use Oathu2.0 to make the application more secure.
* Since it is communication between microservices we can think about, federated identity pattern or we can
  think side car model or we can think about using JWT token based on the need.
* Implementing security using Basic Authentication for time being.

### Authentication using okta
* Use the okta for authentication the app
### Authorization using okta
* For brevity, we want to create only two roles
1) User - Can do CRUD operations on Client API. If required roles further
   added for read access and write access and allow appropriate functionalities.
2) Admin - In addition to User access, admin role user can access the health check of the
   service.
* Validate the request and response type
* If content type is not matched we reject the request
* _csrf token can be sent for update operations if being passed from the client UI.
* All methods procted with the hasAuthorization code.
* I have limited the logging because of being a production grade application. Logging can be enabled by changing
  the parameters in applicaiton.properties
* Proper status codes as per REST standards are returned.
* ResponseEntityExceptionHandler automatically constructs all the general HTTPResponse codes based on the errors.
* Use proper HTTP method according to operation , GET (read), POST (create), PUT (replace/update) and DELETE (delete) a record
* Validate content-type on request Accept header
* Send X-Content-Type-Options: nosniff header on response
* Send X-Frame-Options: deny header.  on response
* We can install cerificates and use HTTPS for production
* OKta collects just email, fist name and last name of the user. No additional information needed.
* SQL queires are geneated by spring so SQL injection will not happen, as all queries generated are 
parameterized.
 


# Controller Design
Using sychronous controller for this project

Controller can be made synchronous and asynchronous based on what we want to implement
* Synchronous - Use Spring MVC and Spring Web dependencies.
* Asynchronous - Use Spring Reactive API - Spring Web Flux. Since the database is still synchronous, just
  changing the controller to asynchronous might help delay the problem if there is resource contention
  at the database layer. There are R2DBC – Reactive databases which can be explored based on the use case.

# Choosing the database
There are still some uncertainties in this use case such as

* What is the max load on the API. How much load is there on the system?
* How is the API used ?
* Would there Read or Write Intensive tasks on the API.
 * A read intensive operation can be combined with Caching framework to fasten data access.(Ex: Redis)
 * A write intensive operation on the API ( Ex:Casandra )
* Will there be Frequent changes to schema. ( We can choose between NO SQL/SQL databases)
* Can the database eventual consistent?
* We can use the CAP Theorem to decide the best database suitable for the use case.
* CQRS pattern
* Sharding & Partioning

If we get more information about these aspects we can choose the right database for the given API.
### Database VS InMemory H2 Lite . ?

* The given use case seems to have CRUD operations which are ideal for the relational databases.
* Following the "KISS" principle I am going to choose In memory database H2 as it helps us to start the database when
  the Spring boot application comes up.
* On the another hand we can create any relational  database such as  MYSQL. For ease of installation/testing by the
  reviewer I am choosing an In memory
  store.
* Limitations include - Data limited based on system capacity. H2 is used only for development purposes.

### UI design
I am assuming this service is mostly used by other micro services so not thinking about the API any more. 

### Repository Pattern
* Spring JPA supports pagination. We can use PagingAndSortingRepository to implement pagination for 
findAll products if required.
* Repository pattern

# Caching
* Based on the use cases we can enable method caching in the Spring layer to improve performance.
  I am not going to enable caching for this application as the assumption we made is we care about
  ACID properties.

# DTO Layer
* We can use DTO layer to further change the request object based on the format required for database. 
* For the scope of this exercise I am keeping it simple and not changes are done to the request Obj. 
* We can use DTO layer to model the data coming from the database and vice-versa.
* Used MapStruct to implement the conversion logic. It performs best among similar frameworks.

# Logging
* Used SFL4j for logging.
* Use Rolling file appender for production and console logging for development
* Used MDC to added a request Id to uniquely identify the request passing through the system for easier log
  analysis if splunk or any other Log aggregators are used.

# Exception Handling
* Override the ResponseEntityExceptionHandler to captuer the exceptions and return only the information
  that is required. Not sending the complete stack trace to other services, just the probable reason
  why it failed.
* In dev environment the property server.error.include-stacktrace shows the stack on the Error page
* In production this flag can be disabled to hide the exception stack trace or a ticket can be created
  automatically with the stack trace and not show anything on the front end.For the ease of finding the error in this POC, I have it enabled always in production.For example
  to find the validation errors.
* I have written a GlobalExceptionHandler which can enabled to eat all the exceptions in the code
  and throw a generic message. This can be enabled based on kind of exception handling needed for the application.
* All generic HTTP response types errors are automatically are thrown by this handler and proper  response codes are 
  used
# Testing
* Added Unit test cases to test the controller logic.
* Run test .\mvnw test

# Improvements
* Circuit breaker Pattern for rate limiting.
* We can use HATEOAS principles and construct production options as part of Product DTO. Will be used to navigate
  via the links.
* Decide database on the use case.
* Database now deletes tables once the application is stopped.
* We can use Reactor pattern for HTTP calls to make it asynchronous and we can reactor supported databases 
  like POSTGRess which supports both ACID, scalabiliy and reactor capabilites.
* Common design patterns that can be included in the service based on the requirements.
* Circuit breaker pattern
* Bulkhead
* Caching
* Federated Identity pattern
* Gatekeeper pattern
* Rate Limiting
* Sharding
* Throttling
* Health check( Acutator)
* Monitoring
* Splunk for integration.
* Logging and MDC to trakc the requests.


# Health status - only Admin has access
http://localhost:8082/acutator


http://localhost:8080/api/v1/user/role-user
http://localhost:8080/api/v1/user/role-admin