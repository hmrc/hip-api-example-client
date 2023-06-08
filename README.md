# hip-api-example-client

Service to demonstrate connectivity from the API Hub to HIP.

## Example API
The HIP example API used is "boardgames".

This API has the following scopes to define access:

- read:exemplar-boardgames: read board game information
- read:exemplar-store: read store information
- write:exemplar-boardgames: create and modify board games
- write:exemplar-store: create and modify store information

The endpoint used in this example is /boardgames/findByStatus and that requires the
read:exemplar-boardgames scope.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
