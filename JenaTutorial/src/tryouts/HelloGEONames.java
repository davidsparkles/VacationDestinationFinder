package tryouts;
import java.util.Iterator;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class HelloGEONames {

	public static void main(String[] args) {
		
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
				System.out.println(result.get("Continent") + " - " + result.get("p") + " - " + result.get("o"));
			}
		} finally {
			qexec.close();
		}
	}

}
