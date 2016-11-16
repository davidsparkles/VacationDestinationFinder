
package org.semantic.vacationDestination.api.generated;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.stereotype.Component;

/**
* Resource class containing the custom logic. Please put your logic here!
*/
@Component("apiDestinationResource")
@Singleton
public class DefaultDestinationResource implements org.semantic.vacationDestination.api.generated.DestinationResource
{
	@javax.ws.rs.core.Context
	private javax.ws.rs.core.UriInfo uriInfo;

	/* GET / */
	@Override
	public Response get()
	{
		// place some logic here
		return Response.ok()
			.entity(new Destination()).build();
	}

	/* POST / */
	@Override
	public Response post(final Destination destination)
	{
		String returnString="";
		// place some logic here
		
		
		//if airplane - travelspeed is 762km/h
		//if car - travelspeed is 89,9km/h
		
		
		
		String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"PREFIX rdfs: <https://www.w3.org/2000/01/rdf-schema#>\n" +
				"PREFIX onto: <http://www.ontotext.com/proton/protontop#>\n" +
				"PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
				"PREFIX dbr: <http://dbpedia.org/resource/>\n" +
				"select distinct ?Continent ?o\n" + 
				"where {\n" + 
					"<http://sws.geonames.org/2873891/> onto:locatedIn ?Continent .\n" +
//					"<http://dbpedia.org/resource/Earth> rdf:type ?o\n" +
					"?Continent rdf:type dbo:Continent\n ." +
					"?Continent dbo:language ?o\n" +
				"}\n" + 
				"LIMIT 100";
		
		Query query = QueryFactory.create(queryString);
		
		String serviceStringDBpedia = "http://dbpedia.org/sparql";
		String serviceString = "http://factforge.net/sparql";
		
		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceString, query);
		
		try{
			ResultSet results = qexec.execSelect();
			while(results.hasNext()){
				QuerySolution result = results.next();
//				if(result.contains("settlement"))
				returnString = returnString+ result.get("Continent") + " - " + result.get("p") + " - " + result.get("o") +"\n";
				System.out.println(result.get("Continent") + " - " + result.get("p") + " - " + result.get("o"));
			}
		} finally {
			qexec.close();
		}
		return Response.ok()
				.entity(returnString).build();
	}

}

class HaversineAlgorithm {

    static final double _eQuatorialEarthRadius = 6378.1370D;
    static final double _d2r = (Math.PI / 180D);

    public static int HaversineInM(double lat1, double long1, double lat2, double long2) {
        return (int) (1000D * HaversineInKM(lat1, long1, lat2, long2));
    }

    public static double HaversineInKM(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * _d2r;
        double dlat = (lat2 - lat1) * _d2r;
        double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                * Math.pow(Math.sin(dlong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        double d = _eQuatorialEarthRadius * c;

        return d;
    }

}

