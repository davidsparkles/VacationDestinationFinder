
package org.semantic.vacationDestination.api.generated;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	@SuppressWarnings("unchecked")
	@Override
	public Response post(final Destination destination)
	{
		
		double latitude = 49.4875;
		double longitude = 8.4660;
		double maxElevation = 0;
		double minElevation = 0;
		double effectiveDistance=1000.0;
		int maxPopulation = 100000000;
		int minPopulation = 150000;
		
		String returnString="";
		String restDistance = null;
		String restTemperature = null;
		String restTransportation = null;
		String restLocation = null;
		String specification = null;
		int month = -1;
		
		if(destination.getDistance()!=null) restDistance = destination.getDistance();
		if(destination.getTemperature()!=null) restTemperature = destination.getTemperature();
		if(destination.getTransportation()!=null) restTransportation = destination.getTransportation();
		if(destination.getLocation()!=null) restLocation = destination.getLocation();
		if(destination.getSpecification()!=null) specification = destination.getSpecification();
		if(destination.getMonth()!=null) month = destination.getMonth();
		
		//TODO only testcity
//		try {
//			URI weatherApiURI = new URIBuilder(weather).build();
//			HttpGet apiHttpGet = new HttpGet(weatherApiURI);
//			HttpResponse apiResponse = httpclient.execute(apiHttpGet);
//			String jsonResponse = EntityUtils.toString(apiResponse.getEntity(),"UTF-8");
//			
//			JSONParser jsonParser = new JSONParser();
//			
//			JSONArray months = (JSONArray)((JSONObject)((JSONArray)((JSONObject)((JSONObject)jsonParser.parse(jsonResponse)).get("data")).get("ClimateAverages")).get(0)).get("month");
//			
//			double avgMinTemp = Double.parseDouble(((JSONObject)months.get(month-1)).get("avgMinTemp").toString());
//			
//			System.out.println(avgMinTemp);
//			
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

		if(restTransportation!=null&&restDistance!=null)effectiveDistance=calculateDistance(restTransportation,restDistance);
				
		//TODO calculate distance based on coordinates and compare them with the effectiveDistance
		//TODO query to get the latlong from currentlocation
		
		String settlementBasis =
				"{"+
					"?settlement a dbo:City"+
				"}"+
				"UNION"+
				"{"+
					"?settlement a dbo:Town"+
				"}"+
					"UNION"+
				"{"+
					"?settlement a dbo:Village"+
				"}";

		String settlementBasisSki = 
				"?countryList skos:broader dbc:Ski_areas_and_resorts_by_country ."+
				"?settlement dct:subject ?countryList";

		if(specification.equals("mountain"))settlementBasis=settlementBasisSki;
		
		String defaultSettlementQuery = 
				"PREFIX bif: <bif:>\n" +
				"PREFIX dct: <http://purl.org/dc/terms/>\n"+
				"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"+
				"PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
				"PREFIX dbp: <http://dbpedia.org/property/>\n" +
				"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"+
				"PREFIX dbc: <http://dbpedia.org/page/Category:>\n" +
				"select distinct *\n" + 
				"Where{"+
					settlementBasis+
					"?settlement dbo:populationTotal ?population ."+
					"?settlement dbo:country ?country ."+
					"?settlement dbo:elevation ?elevation ."+
					"?settlement geo:geometry ?point ."+
					"?settlement rdfs:label ?label ."+
					"FILTER(LANG(?label) = '' || LANGMATCHES(LANG(?label), 'en'))";
					
					if(specification.equals("beach")){
						maxElevation=25;
						defaultSettlementQuery=defaultSettlementQuery+
								"FILTER(?population > +"+minPopulation+" && ?population <"+maxPopulation+")"+
								"FILTER(?elevation <"+maxElevation+")";
					}
					else if(specification.equals("mountain")&&effectiveDistance<2000){
						minElevation=550;
						minPopulation=100000;
						defaultSettlementQuery=defaultSettlementQuery+
								"FILTER(?population > +"+minPopulation+" && ?population <"+maxPopulation+")"+
								"FILTER(?elevation >"+minElevation+")";
								
					}
					else if(specification.equals("mountain")&&effectiveDistance>2000){
						minElevation=1500;
						defaultSettlementQuery=defaultSettlementQuery+"FILTER(?elevation >"+minElevation+")";
					}
					
					defaultSettlementQuery=defaultSettlementQuery+"FILTER(bif:st_intersects (?point, 'POINT("+longitude+" "+latitude+")"
							+ "'^^<http://www.openlinksw.com/schemas/virtrdf#Geometry>, "+effectiveDistance+")) ." +
			    	
				"}"+
					"ORDER BY ?population";
		
		Query query = QueryFactory.create(defaultSettlementQuery);
		
		String serviceStringDBpedia = "http://dbpedia.org/sparql";
		String serviceString = "http://factforge.net/sparql";
		
		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceStringDBpedia, query);
		JSONObject returnObject = new JSONObject();
		ArrayList<String> possibleCities = new ArrayList<>(); 
		try{
			ResultSet results = qexec.execSelect();
			while(results.hasNext()){
				JSONArray cityArray = new JSONArray();
				JSONObject latCoord = new JSONObject();
				JSONObject longCoord = new JSONObject();
				String label="";
				QuerySolution result = results.next();
				List<String> variables = results.getResultVars();
				for(int i =0;i< results.getResultVars().size();i++){
					String value = result.get(variables.get(i)).toString();
					if(variables.get(i).equals("label")){
						label= value.substring(0, value.indexOf("@"));
					}else if(variables.get(i).equals("point")){
						Pattern p = Pattern.compile("\\d+\\.\\d+\\s\\d+\\.\\d+");
						Matcher m = p.matcher(result.get(variables.get(i)).toString());
						if(m.find()){
							String latLong= m.group(0);
							latCoord.put("lat", latLong.substring(0, latLong.indexOf(" ")));
							longCoord.put("long", latLong.substring(latLong.indexOf(" ")+1));
						}
					}
					returnString = returnString+ "\n"+variables.get(i)+" : "+result.get(variables.get(i));
					System.out.println(variables.get(i)+" : "+result.get(variables.get(i)));					
				}
				cityArray.add(latCoord);
				cityArray.add(longCoord);
				returnObject.put(label, cityArray);
				possibleCities.add(label);
			}
		} finally {
			qexec.close();
		}
		
		if(restTemperature!=null&&month!=-1){
			HttpClient httpclient = HttpClients.createDefault();

			for(String cityName:possibleCities){
				try {
					String fullName=cityName;
					if(cityName.contains("("))cityName=cityName.substring(0, cityName.indexOf("(")-1);
					if(cityName.contains(","))cityName=cityName.substring(0, cityName.indexOf(","));
					String weather ="http://api.worldweatheronline.com/premium/v1/weather.ashx?key=40dd3fd2475942d48a6140651161611&q="+cityName+"&format=json&fx=no&cc=no&mca=yes";
					URI weatherApiURI = new URIBuilder(weather).build();
					HttpGet apiHttpGet = new HttpGet(weatherApiURI);
					HttpResponse apiResponse = httpclient.execute(apiHttpGet);
					String jsonResponse = EntityUtils.toString(apiResponse.getEntity(),"UTF-8");

					JSONParser jsonParser = new JSONParser();

					JSONArray months = (JSONArray)((JSONObject)((JSONArray)((JSONObject)((JSONObject)jsonParser.parse(jsonResponse)).get("data")).get("ClimateAverages")).get(0)).get("month");

					double avgMinTemp = Double.parseDouble(((JSONObject)months.get(month-1)).get("avgMinTemp").toString());

					if((Double.parseDouble(restTemperature))>avgMinTemp){
						returnObject.remove(fullName);
					}

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
			}

		}
		
		return Response.ok().header("Content-Type", "application/json")
				.entity(returnObject).build();
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

