# Stock Service

This is an example of API REST service that handles the stocks. A stock is an item that contains its identifier, the name of the stock, the current stock price and the timestamp of the last update.
The service is a Spring Boot application using H2 as database.

## Dependencies:

This is the list of dependencies you will need to run the project:

- Java 8 or later.
- Gradle 5 (5.6.x only) or Gradle 6 (6.3 or later)
 
## How to run it:

The application will be deployed in the domain localhost through the port 8080 by default.

- Run application using Gradle under project directory


    gradle bootRun

- Run application using Java under project directory


    gradle build

    java -jar build/libs/stock-api-1.0-SNAPSHOT.jar

## Additional configuration:

Use the following properties to customize the application modifying _application.properties_

1. Default port. The default port may be change to any other adding the following property:


    server.port=8080

2. H2 console. H2 with Spring boot allows to enable a web h2 client with the following properties:


    spring.h2.console.enabled=true

    spring.h2.console.path=/h2

3. H2 allows configuring whether to use in-memory database or physical file database changing the following properties:


    spring.datasource.url=jdbc:h2:mem:testdb

    #spring.datasource.url=jdbc:h2:file:./stock-database

## Endpoints

The stock API provides the following endpoints including cURL examples running the application locally.


### List stocks

Retrieves a list of all existing stocks

- Method: GET
- URI: /api/stocks
- Content-Type: \*/\*

Possible responses are:

- Http 200 Ok: It returns the existing items

**Example cURL request**

    curl --location --request GET 'http://localhost:8080/api/stocks'

**Example response**

    HTTP 200 OK
    [
        {
            "id":1,
            "name":"Apple Inc",
            "currentPrice":141.2,
            "lastUpdate":"2021-01-26T13:28:20.299+00:00"
        },
        {
            "id":2,
            "name":"Microsoft Corp",
            "currentPrice":225.66,
            "lastUpdate":"2021-01-26T13:28:20.299+00:00"
        }
    ]

### Get stock by id

Retrieves one single stock if exists providing an identifier

- Method: GET
- URI: /api/stocks/{stockId}
- Content-Type: \*/\*

Possible responses are:

- Http 200 Ok: The object has been found and returned
- Http 404 Not Found: the stock has not been found

**Example cURL request**

    curl --location --request GET 'http://localhost:8080/api/stocks/2'

**Example response**

    HTTP 200 OK
    {
        "id":2,
        "name":"Microsoft Corp",
        "currentPrice":225.66,
        "lastUpdate":"2021-01-26T18:27:20.299+00:00"
    }


### Create stock

Creates a new stock given a name, and a price

- Method: POST
- URI: /api/stocks
- Content-Type: application/json

Possible responses are:

- Http 201 Created: the object has been successfully created
- Http 400 Bad request: at least one parameter is not valid. See requirements below

#### Requirements

- Name must be provided and length up to 250 characters
- Price is mandatory and must be a positive number

**Example cURL request**

    curl --location --request POST 'http://localhost:8080/api/stocks' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "name": "Readme",
        "currentPrice": 1120.0
    }'

**Example response**

    HTTP 201 Created
    {
        "id": 22,
        "name": "Readme",
        "currentPrice": 1120.0,
        "lastUpdate": "2021-01-26T18:30:30.379+00:00"
    }

### Update stock

Updates an existing stock given a name, and a price.

- Method: PUT
- URI: /api/stocks/{stockId}
- Content-Type: application/json

Possible responses are:

- Http 204 No content: the object has been successfully updated
- Http 400 Bad request: at least one parameter is not valid. See requirements below
- Http 404 Not Found: the stock has not been found

#### Requirements

- Stock must exist
- Name must be provided and length up to 250 characters
- Price is mandatory and must be a positive number

**Example cURL request**

    curl --location --request PUT 'http://localhost:8080/api/stocks/22' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "name": "Updated",
        "currentPrice": 1120.0
    }'

**Example response**

    HTTP 204 No Content
