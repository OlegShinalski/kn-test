# Contacts
The project is a simple city list web application with functions of listing cities, searching by name, paging and editing city name and picture URL. 
The back-end is the **Java Spring Boot** application built using **Maven** and connected to the in-memory **H2 database**. The front-end was developed with **Angular 10**. Automated tests was created using **Junit** and **Mockito**.
The fromnend is written on Angular.

## Prerequisites
- **NodeJS** - https://nodejs.org/en/
- **IntelliJ IDEA** - https://www.jetbrains.com/idea/
- **JDK** - https://www.oracle.com/java/technologies/javase-downloads.html. In this project is used Java 17 (Amazon Correto 17.0.7 distibution).

## Application Structure
### Back-end
Used H2 in-memory database.
For database initialisation used Liquibase.
Initial data are in `src\main\resources\cities.csv` file, which is imported by Liquibase.
Implemented server-side pagination.

The server project is stored in the `src\` directory. It contains *controller*, *service*, *repository* and *entity* packages inside the `src\main\java\ee\kuehnenagel\contacts\` package.
- `CityRestController` contains the limited *REST API* (only `GET` and `PUT` requests).
- `CityService` get and returns transfer objects `CityDto`. 
	 - `getCities` returns page of cities.
	 - `save` save the city.
	 - `getById` returns city bt Id.
- `CityRepository` extends `PagingAndSortingRepository` and `CrudRepository` interfaces. That allows to create easily *query* methods using keywords. It contins `findByNameContainsIgnoreCase` method that implement a LIKE *query* and ignore case.
- `City` is an *entity* that contains contact id, name and photo url fields. These fields are linked with database columns.
- `CityDto` is a *data transfer object* on UI level.
- `CityMapper` convert entity from / into transfer object.
- `cities.csv` file is stored in the `src\main\resources` directory. It imported into database using Liquibase.
- `application.properties` provides a list of database settings and a CSV file path.

### Unit Tests
The `src/test/java/com/example/kntest` package contains unit tests for *controller*, *service* and *transformer*. *repository* is not tested because no custom logic, all funcionality used in-box.  

There is also `people-test.csv` file for testing in `src\test\resources` directory and its path is stored in `application-test.properties`.

### Front-end
The client project is stored in the `frontend\` directory.
- `cities-list` component - responsible for displaying list of cities (with pagination)
- `city-details` component - responsible for editing city details
- `city.service.ts` contains methods for back-end communication.

## Installing & Running 
1. Clone this repo https://github.com/puchkova/contacts.git

2. Import project into Your IDE, build and use your IDE `Run` commant to run the server application

3. Or in root project directory :
   3.1 mvn clean install - build whole project, run unit tests
   3.2 mvn spring-boot:run - run application

4. Go to the `frontend` directory in your terminal and run the `npm install` command to install node modules 

5. Run the `nmp start` command to run the client application

6. Now the application is available on http://localhost:4200/

7. Right click on the `contact` module in your IDE and choose `Run 'All Tests'` to run automated tests 
