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

public class HelloRDFWorld {

	public static void main(String[] args) {
		
//		String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
//				"PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
//				"PREFIX dbr: <http://dbpedia.org/resource/>\n" +
//				"select distinct ?settlement ?populationTotal\n" + 
//				"where {\n" + 
//					"?settlement rdf:type dbo:City ;\n" + 
//						"dbo:populationTotal ?populationTotal ;\n" +
//						"dbo:country dbr:Germany .\n" +
//					"FILTER ( ?populationTotal > 1000000 )\n" +
//				"}\n" + 
//				"LIMIT 100";
		
		String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
				"PREFIX dbp: <http://dbpedia.org/property/>\n" +
				"PREFIX dbr: <http://dbpedia.org/resource/>\n" +
//				"select distinct ?Concept where {dbr:Mannheim <http://dbpedia.org/property/janLowC> ?Concept} LIMIT 100"
				"select distinct ?p\n" + 
				"where {\n" + 
					"dbr:Mannheim dbp:junHighC ?p\n" + 
//						"dbo:populationTotal ?populationTotal ;\n" +
//						"dbo:country dbr:Germany .\n" +
//					"FILTER ( ?populationTotal > 1000000 )\n" +
				"}\n" + 
				"LIMIT 100"
		;
		
		Query query = QueryFactory.create(queryString);
		
		String serviceStringDBpedia = "http://live.dbpedia.org/sparql";
		
		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceStringDBpedia, query);
		
		try{
			ResultSet results = qexec.execSelect();
			while(results.hasNext()){
				QuerySolution result = results.next();
				if(result.contains("p")) System.out.println(result.get("p") + " - " + result.get("populationTotal"));
			}
		} finally {
			qexec.close();
		}
	}

}
