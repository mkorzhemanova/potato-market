# potato-market

## How to start the potato-market application

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/potato-market-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

## Health Check

To see your applications health enter url `http://localhost:8081/healthcheck`

## Potato Market

### Potato Bags

####  Potato Bags List Endpoint
| HTTP Method | Path | Notes |
| --- | --- | --- |
| GET | potato-market/potato-bags | Get a list of potato bags for sale in the market |

##### Parameters
###### Query string parameters
| Query string parameter | Required / Optional | Description | Type |
| --- | --- | --- | --- |
| size | Optional | The number of bags to include in the response. Default is 3 | Integer |



####  Add Potato Bag Endpoint
| HTTP Method | Path | Notes |
| --- | --- | --- |
| POST | potato-market/potato-bags | Add a new potato bag to the market |

##### Parameters
###### Body parameter
Body is represented as JSON object of the following structure

| Field | Required / Optional | Description | Type |
| --- | --- | --- | --- |
| id | Mandatory | Bag identifier | String |
| potatoCount | Mandatory | Number of potatoes in a bag | Integer |
| supplier | Mandatory | Supplier of a bag | String (one of "De Coster", "Owel", "Patatas Ruben", "Yunnan Spices") |
| packageDateTime | Mandatory | Date and time when a bag was packed | The ISO date-time format (a date-time with an offset). Example: '2011-12-03T10:15:30+01:00'. |
| price | Mandatory | Price | Decimal |

[Sample Potato Bag JSON](src/test/resources/fixtures/potatoBag.json)
 

