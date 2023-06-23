# hip-api-example-client

Service to demonstrate connectivity from the API Hub to HIP using the
example Boardgames API.

For more information on the project please visit this space in Confluence:
https://confluence.tools.tax.service.gov.uk/display/AH/The+API+Hub+Home

## Requirements

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), so needs at least a [JRE] to run.

## Dependencies
Beyond the typical HMRC Digital platform dependencies this service relies on:
- Boardgames API in HIP

You can view service dependencies using the Tax Catalogue's Service Relationships
section here:
https://catalogue.tax.service.gov.uk/service/hip-api-example-client

### Boardgames API
The HIP example API used is "boardgames".

This API has the following scopes to define access:

- read:exemplar-boardgames: read board game information
- read:exemplar-store: read store information
- write:exemplar-boardgames: create and modify board games
- write:exemplar-store: create and modify store information

The endpoint used in this example is /boardgames/findByStatus and that requires the
read:exemplar-boardgames scope.

This service must be configured with suitable credentials to call the
Boardgames API. See the `microservice.services.example-api` configuration
settings in `application.conf`.

Note that the Boardgames API is only deployed in QA and production and that this
service is not intended to run in any other environment.

## Using the service

### Running the application

To run the application use `sbt run` to start the service.

Note that this service cannot actually function locally as the Boardgames
API is only deployed in QA and production.

### Authentication
This service authenticates incoming requests using internal-auth's service to service
pattern. See https://github.com/hmrc/internal-auth for details.

## Building the service
This service can be built on the command line using sbt.
```
sbt compile
```

### Unit tests
This microservice has many unit tests that can be run from the command line:
```
sbt test
```

### Integration tests
This microservice has some integration tests that can be run from the command line:
```
sbt it:test
```

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
