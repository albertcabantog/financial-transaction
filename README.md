# README #

System that analyses financial transaction records

## Prerequisite ##
The application is written in Java with third party utilities.  The following are required to be installed to build and run the application:

* OpenJDK Java 11
* Maven 3.6.2
* Git 2.25.1

## Building the application ##

* Clone this code base

```
git clone https://github.com/albertcabantog/financial-transaction.git
```


* Run Maven inside the financial-transaction/transaction-analyser directory to build the application without executing the unit tests

```
cd financial-transaction/transaction-analyser
mvn clean install -DskipTests
```

## Running the unit tests ##

All unit test will be executed and the subsequent report will be generated.  Here is to run the test:

* Run `mvn test`
* Test reports will be written in `target/surefire-reports`

## Packaging the application ##

To package the application, run the maven command:
```
mvn clean test package
```
This will build and create the jar file to be used for execution.

## Running the application ##

After packaging the application, an executable jar file will be created to execute the application.
This jar file will be created under the target folder.  Below is the command to run the application:

```
java -jar target/transaction-analyser-1.0-SNAPSHOT-jar-with-dependencies.jar "ACC334455" "20/10/2018 12:00:00" "20/10/2018 19:00:00"
```
* `java -jar target/transaction-analyser-1.0-SNAPSHOT-jar-with-dependencies.jar ` - this is the generated jar file
* `"ACC334455"` - this is the parameter for account id
* `"20/10/2018 12:00:00"` - this is the date from parameter
* `"20/10/2018 19:00:00"` - this is the date to parameter

_Please note the parameters are enclosed with double quotes._

Sample output:
```
Input arguments:
accountId: ACC334455
from: 20/10/2018 12:00:00
to: 20/10/2018 19:00:00
Output:
Relative balance for the period is: -25.00
Number of transactions included is: 1
```

## Changing the list of input transaction ##
The default list of the transaction are listed in the **transaction_list.csv** file.  This file is located in:
`transaction-analyser/src/main/resources`

To update the contents, edit this file and package again the application.

## Assumptions ##
* Input file and records are all in a valid format
* Transaction are recorded in order
