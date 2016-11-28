# VacationDestinationFinder
Find a place based by semantic web technologies

# Installation

Clone git or download files. Add Jena-Libaries. Compile those java files.

Then you are ready to execute the class files and explore the world of linked open data with Apache Jena, Java and SPARQL.

Have fun!

## Backend

After Downloading:
```
mvn clean install
mvn eclipse:m2eclipse
```
and then import the project

## Frontend


Install ionic framework [https://ionicframework.com/]:
```
npm install -g cordova ionic
```
In the 'vacationFrontend' folder:
```
npm install
ionic serve
```
Your internet browser should open.

#Sample Request

Request:
```
POST /destination HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{"month":"8","distance":"12h","transportation":"car","specification":"beach","temperature":"18"}
```

Response
```
{
  "Split, Croatia": [
    {
      "lat": "16.450000762939"
    },
    {
      "long": "43.509998321533"
    }
  ]
}
```
