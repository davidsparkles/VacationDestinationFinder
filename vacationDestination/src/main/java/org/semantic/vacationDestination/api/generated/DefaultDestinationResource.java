
package org.semantic.vacationDestination.api.generated;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
		
		String restDistance = destination.getDistance();
		String restTemperature = destination.getTemperature();
		String restTransportation = destination.getTransportation();
		String restLocation = destination.getLocation();
		int month = destination.getMonth();
		
		//TODO only testcity
		String city="Stuttgart";
		String a ="http://api.worldweatheronline.com/premium/v1/weather.ashx?key=40dd3fd2475942d48a6140651161611&q="+city+"&format=json&fx=no&cc=no&mca=yes";
		HttpClient httpclient = HttpClients.createDefault();
		try {
			URI apiURI = new URIBuilder(a).build();
			HttpGet apiHttpGet = new HttpGet(apiURI);
			HttpResponse apiResponse = httpclient.execute(apiHttpGet);
			String jsonResponse = EntityUtils.toString(apiResponse.getEntity(),"UTF-8");
			
			JSONParser jsonParser = new JSONParser();
			
			JSONArray months = (JSONArray)((JSONObject)((JSONArray)((JSONObject)((JSONObject)jsonParser.parse(jsonResponse)).get("data")).get("ClimateAverages")).get(0)).get("month");
			
			Double avgMinTemp = Double.parseDouble(((JSONObject)months.get(month-1)).get("avgMinTemp").toString());
			
			System.out.println(avgMinTemp);
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Double effectiveDistance=-1.0;
		if(restTransportation!=null&&restDistance!=null)effectiveDistance=calculateDistance(restTransportation,restDistance);
		
		//TODO calculate distance based on coordinates and compare them with the effectiveDistance
		//TODO query to get the latlong from currentlocation
		
		//TestPushComment
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
		
		//TODO build JSON return
		JSONObject returnObject = new JSONObject();
		JSONArray cityArray = new JSONArray();
		JSONObject latCoord = new JSONObject();
		JSONObject longCoord = new JSONObject();
		
		cityArray.add(latCoord);
		cityArray.add(longCoord);
		returnObject.put(city, cityArray);
		
		return Response.ok()
				.entity(returnString).build();
	}

	private Double calculateDistance(String restTransportation, String restDistance) {
		Double travelspeed=-1.0;
		if(restTransportation.equals("car"))travelspeed=89.9;
		else if(restTransportation.equals("plane"))travelspeed=762.0;
		Double effectiveDistance=Double.parseDouble(restDistance.substring(0,restDistance.indexOf("h")));
		return travelspeed*effectiveDistance;
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

