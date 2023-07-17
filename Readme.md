## Assessment

### Problem Statement : 
A retailer offers a rewards program to its customers, awarding points based on each recorded purchase. A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent between $50 and $100 in each transaction. (e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points). Given a record of every transaction during a three month period, calculate the reward points earned for each customer per month and total.

### Prerequisites
1. Java 11
2. IDE to run Spring-boot application (Eclipse, Spring Tool Suite, IntelliJ Idea)

### How to run 
1. In IDE go to src/main/java/com/chartercommunication/assesment/RetailPointsApplication/RetailPointsApplication.java and click on run.
2. If IDE not available, Go to app root directory i.e. src/main/java/com/chartercommunication/assesment/RetailPointsApplication and use CLI command ```mvn spring-boot:run```

### Solution
1. Since there was no DataSet given, So Added a JSON file with mock data  and used command line runner to run a script which adds all the data to the respective database tables
2. Used H2 database to reduce overhead of setting up a database (To open use "localhost:8080/h2-console" if running on local)
3. There is method in CustomerService "getReport()" using "localhost:8080/customer/report" if running on local to Get a report of CustomerInfo in which Data is firstly grouped by CustomerID then by MonthOfPurchase and then Flattened from Map to a ObjectList where each object includes customer info and inside it a list of object "Month:PointsOfThatMonth". 
4. Example Output : 
```
   "{
       "customerId": "01H5913KBFH14DTF875CA6G6Y0",
       "customerName": "Armstrong Ennals",
       "pointsPerMonth":    
        [{
           "month": "JUNE",
           "points": 68
           },  
           {
           "month": "JULY",
           "points": 56
        }],
       "totalPoints": 124
   }"
   ```

### Database Schema
#### CUSTOMER
1. ID : Integer
2. CREATED_AT : TIMESTAMP
3. CUSTOMER_ID : Varchar (ULID)
4. CUSTOMER_NAME : Varchar

#### PURCHASE_DETAILS
1. ID : Integer
2. CREATED_AT : TIMESTAMP
3. CUSTOMER_ID : Integer (ID in Customer Table)
4. POINTS : Integer
5. PURCHASE_TIMESTAMP : TIMESTAMP

### Hence We were able to fulfill the requirement of the assessment