
This Silver Bars back-end server implemented  using two profiles. "db_persisted" uses Jpa repository and database connection to store Orders and running with "cache_persisted" profile it will 
simply use Concurrent Hash Map for caching Orders into the memorry. Database implementation could be easely extedent for a Production use where Map version could be use for a quick junit/integration test runs.

Running 'mvn clean install' build will run integration tests and build standalone springboot jar (order-board-1.0-SNAPSHOT.jar):


The jar could be executed on the server as following (it will run sprinboot with embeded tomcat server):

#run using h2 database from build dir:
-Dspring.profiles.active=db_persisted [optional: -Dinit.orders.file.path="<initial orders file path>"] -jar ./target/order-board-1.0-SNAPSHOT.jar

#run using concurrent hash map:
java -Dspring.profiles.active=cache_persisted [optional: -Dinit.orders.file.path="<initial orders file path>"] -jar ./target/order-board-1.0-SNAPSHOT.jar



#Rest endpoints:

#make new order  http://localhost:8080/register
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"id":6,"userId":"USER_2","orderQuantity":1.0,"orderPrice":500,"buySell":"BUY"}' \
  http://localhost:8080/register

#return  all registred orders so far
http://localhost:8080/getallorders
  
#cancel an order by order id 
http://localhost:8080/cancel?orderId=2

#return aggregated summary of all orders
http://localhost:8080/aggregate

#return single order by order id
http://localhost:8080/getorder?orderId=2

