package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;

public class Ontology {
	
	public static final String SOURCE = "animals-vh.owl";	
	//ontology namespace
	public static final String NAME_SPACE = "http://a.com/ontology#";
	
	public static void main(String[] args) throws IOException {
		
		// create the base model
		OntModel base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM );
		base.read( SOURCE, "RDF/XML" );

		// create the reasoning model using the base
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, base);
   
      OntClass artefact = model.getOntClass(NAME_SPACE + "Fish");
        System.out.println("for : " + artefact.getURI());
        System.out.println("Sublcass : ");
        for (Iterator<OntClass> i = artefact.listSubClasses(); i.hasNext();) {
        	OntClass c = i.next();
        	if (c != null) {
        		System.out.println(c.getURI());
        	}
        }
        
        System.out.println("prps :");
        for (Iterator<OntProperty> ii = artefact.listDeclaredProperties(); ii.hasNext();) {
        	OntProperty c = ii.next();
        	System.out.println(c.getURI().replace("http://a.com/ontology#",""));
        }
        System.out.println("equivalent :");
        for (Iterator<OntClass> iii = artefact.listEquivalentClasses(); iii.hasNext();) {
        	OntClass c = iii.next();
        	System.out.println(c.getURI().replace("http://a.com/ontology#",""));
        }
        
        System.out.println("super :");
        for (Iterator<OntClass> iiii = artefact.listSuperClasses(); iiii.hasNext();) {
        	OntClass c = iiii.next();
        	System.out.println(c.getURI().replace("http://a.com/ontology#",""));
        }

       
    		      
       String prefix = "prefix animals: <" + NAME_SPACE + ">\n" +
                "prefix rdfs: <" + RDFS.getURI() + ">\n" +
                "prefix owl: <" + OWL.getURI() + ">\n";


/*			showQuery( model,
			           prefix +
			           "select ?animals ?something where { ?animals animals:EatenBy ?something ;"
			           		+ "}" ); */

	}
	

    
    protected static void showQuery(Model m, String q) {
        Query query = QueryFactory.create( q );
        QueryExecution qexec = QueryExecutionFactory.create( query, m );
        try {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query);
        }
        finally {
            qexec.close();
        }
    }
    
//	File f = new File("/Users/remi/Documents/dev/galata/elcipse/ontology2.owl");
//	InputStream in = new FileInputStream(f);
//	OntModel model2 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
//	model2.read(in,null);
//	in.close();
	

//	Iterator<OntClass> it = m.listClasses();
//	while (it.hasNext()) {
//			OntClass ontclass = it.next();
//			System.out.println(ontclass.getLabel(null));
//		}

    // Create a new query
//       String queryString =        
//       "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
//        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  "+
//        "select ?uri "+
//           "where { "+
//         "?uri rdfs:subClassOf <http://www.co-ode.org/roberts/pto.owl#Charge>  "+
//        "} \n ";
 //   Query query = QueryFactory.create(queryString);


    // Execute the query and obtain results
//    QueryExecution qe = QueryExecutionFactory.create(query, model2);
//    ResultSet results =  qe.execSelect();

    // Output query results    
 //   ResultSetFormatter.out(System.out, results, query);

}
