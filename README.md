# VacationDestinationFinder
Find a place based by semantic web technologies

# Installation

Clone git or download files. Add Jena-Libaries. Compile those java files.

Then you are ready to execute the class files and explore the world of linked open data with Apache Jena, Java and SPARQL.

Have fun!

After Downloading:
```
mvn clean install
mvn eclipse:m2eclipse
```
and then import the project


#Sample Request

Request:
```
POST /destination HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: bc1eeda7-5807-dd78-b1f6-654067b1cc90

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
      "long": " 43"
    }
  ]
}
```
