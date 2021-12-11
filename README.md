CandleStick Service
====================

Code Structure
---------

### Instrument
The Instrument class models an Asset. The Instruments are uniquely identified by their isin.

### Quote
The Quote class models a change in the price of an Instrument. The quote is identified by isin and time.

### CandlestickAction
The CandlestickAction is the entry point for the "/candlesticks" API. It retrieves the candlesticks info for the duration required for Customer. In this case we fetch the last 30 minutes data, with 1 minute chunks of candlesticks.

### Solution Approach 

The data is continously listened thorugh the websockets and it is persisted in a database. The type of database was one thing to get it clear from the start. If it was just "quotes" data, a time series database could have been chosen like InfluxDB. But since we needed to manage Instrument data also, the need for a relational database was important. So I went ahead with Postgres which has a TimescaleDb built as an extension for it. This solved both the problems. An endpoint for the customer is exposed which returns the time-interval data. This can be achieved in a single query through TimeScaleDB. TimeScaleDB has a better performance compared to Postgres alone. Refer to this article for more insights regarding the same. (https://blog.timescale.com/blog/timescaledb-vs-6a696248104e) 


### Exception handling
Service throws a custom 'InstrumentNotFoundException' if the isin is not present in the catalogue or it was deleted from the catalogue.
Service throws a custom 'QuotesNotFoundException' if there are no quotes data available for the time frame (last 30 minutes) when the user requests data. 
The data may not be available in two cases.
1. If the instrument was deleted, we do not maintain the quotes data or receive any quotes.
2. If no quotes were received in the last 30 minutes time frame for the instrument.

### Application Properties

#### Database Properties <br />
app.datasource.driver=<postgressql_driver><br />
app.datasource.url=<postgressql_url><br />
app.datasource.username=<username><br />
app.datasource.password=<password><br />

#### Assumptions

1. When a message of type delete is received from "/instruments", we need to delete it from the inventory. The historical price data will also be deleted.
2. The user requests the information for an instrument that has 30 minutes worth of data. 

### Enhancements

1. Implementing a Kafka that queues the input data from the WebSockets and persists to DB asynchronously. This will reduce the overhead of DB calls/second. There might be some lag, but that can be compromised.
2. Packaging as a war using maven-assembly plugin for deployment on external servers.
3. Logging style enhancements using custom log back config file.
4. Documentation of APIs using Swagger.


### Technology Used

1. Java 1.8 + Maven + ApacheStruts2
2. Postgres + TimeScaleDB 
4. Embedded Tomcat server to expose APIs

Running
---------------------
### Database 

Postgres DB along with TimescaleDB should be present in the local machine to persist data. 
Installation links and setup links are given below.

#### Postgres

https://www.postgresql.org/docs/9.3/tutorial-install.html

#### TimeScale DB
https://docs.timescale.com/install/latest/self-hosted/installation-debian/#installing-self-hosted-timescaledb-on-debian-based-systems

Once installed migration of table is required. Run the **V1__initdb.sql** file from the src/main/resources folder to run the initial data migrations. 
Run the below command to import the tables to the system. 

 1. psql -U postgres < V1__initdb.sql 

### Server

Requires Maven and Java 8 to be installed on a Unix box.

### Using Maven and source code - during development

	mvn tomcat7:run



### Deplaying the war file by starting a tomcat server

 1. Unzip the apache-tomcat-8.5.71 provided. 
 2. Go to the webapps folder once unzipped and paste the war file.
 3. Go to bin folder and then run the below commands to start a server <br />
	
 `    sh startup.sh      `

This automatically starts an embedded server on port 8080 with context `CandlestickManager`. 



### API and Sample Output

Running on localhost

1. Find the last 30 minutes Quotes for Instrument 'JX00162I3551'.

` http://localhost:8080/CandlestickManager/candlesticks?isin=JX00162I3551

```javascript
{"JX00162I3551":
	[
		{
			"closed_price":1400.641,
			"high_price":1400.641,
			"low_price":1400.641,
			"open_price":1400.641,
			"open_timestamp":"2021-12-10 21:20:00.0",

		},
		{
			"closed_price":1376.359,
			"high_price":1395.5385,
			"low_price":1352.0769,
			"open_price":1375.2564,
			"open_timestamp":"2021-12-10 21:19:00.0",

		},
		{
			"closed_price":1357.9744,
			"high_price":1399.2821,
			"low_price":1357.9744,
			"open_price":1399.2821,
			"open_timestamp":"2021-12-10 21:18:00.0",
		}
	]
}
```

We can also add additional params to the API 

`duration`: The last time duration for record needed. By default, it is set to 30 <br />
`chunk`: The size of the candlesticks <br />
