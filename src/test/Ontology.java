package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.ontology.Restriction;
import org.apache.jena.ontology.UnionClass;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.shared.PropertyNotFoundException;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
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
   
      OntClass artefact = model.getOntClass(NAME_SPACE + "Penguin");
      
      for (Iterator<OntClass> supers = artefact.listSuperClasses(); supers.hasNext(); ) {
          displayType( supers.next() );
      }
      
      String test = "";
      for (Iterator<OntClass> supers = artefact.listSuperClasses(); supers.hasNext(); ) {
    	  test = test + getAnimalInformation(supers.next());
      }
      System.out.println(test);
      
      
      System.out.println(artefact.hasSubClass());
      System.out.println("sub :");
      for (Iterator<OntClass> iiii = artefact.listSubClasses(); iiii.hasNext();) {
      	OntClass c = iiii.next();
      	if (c != null || c.getURI().toString() != "null") {
      		System.out.println(c.getURI());
      	} 
      }
      System.out.println();
      System.out.println();
      System.out.println();

      System.out.println("disjoint : ");
      for (Iterator<OntClass> i = artefact.listDisjointWith(); i.hasNext();) {
      	OntClass c = i.next();
      	if (c != null) {
      		System.out.println(c.getURI());
      	}
      	if (c.hasSubClass() == true) {
      		for (Iterator<OntClass> ii = c.listSubClasses(); ii.hasNext();) {
            	OntClass x = ii.next();
            	if (c != null) {
            		System.out.println(x.getURI());
            	}
            }
      	}
      	
      } 
      System.out.println();
      System.out.println();
      System.out.println();
      System.out.println();
      
   // Get the property and the subject
      Property HasHabitat = model.getProperty(NAME_SPACE + "ObjectProperty");
      //Resource bus = model.getResource(NAME_SPACE + "bus");

      // Get all statements/triples of the form (****, driverOf, bus)
      //org.apache.jena.rdf.model.Model model2 = (org.apache.jena.rdf.model.Model) model;
      
      //OntModel fixedModel = ModelFactory.createOntologyModel(model.getSpecification());
      //fixedModel.add(model);
      
 /*     System.out.println("list of statements : ");
      StmtIterator stmtIterator = model.listStatements(artefact, null, (RDFNode)null);
      while (stmtIterator.hasNext()) {
          Statement s = stmtIterator.nextStatement();
          System.out.println(s);
	} */
      
    
      
        
      /*     System.out.println("prps :");
        for (Iterator<OntProperty> ii = artefact.listDeclaredProperties(); ii.hasNext();) {
        	
        	OntProperty c = ii.next();
        	System.out.println(c.getLocalName());
        	
        	      	
        } */
       
      
        System.out.println("equivalent :");
        for (Iterator<OntClass> iii = artefact.listEquivalentClasses(); iii.hasNext();) {
        	OntClass c = iii.next();
        	if (c != null) {
        		System.out.println(c.getURI());
        	}
        }
        
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        
        System.out.println("super :");
        for (Iterator<OntClass> iiii = artefact.listSuperClasses(); iiii.hasNext();) {
        	OntClass c = iiii.next();
        	if (c != null || c.getURI().toString() != "null") {
        		System.out.println(c.getURI());
        	} 
        }

       
    /*		      
      
				
	}
	
	
	/*?subClass rdfs:subClassOf animals:Fish ."
			           			+ "?property rdfs:range animals:Fish ;"
			           			+ " ?property rdfs:domain animals:Fish ; */
	

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
    
    static String getAnimalInformation(OntClass sup) {
    	if (sup.isRestriction()) {
            return displayRestrictionForAnimal( sup.asRestriction() );
        } else {
        	return "";
        }
    }
    static String displayRestrictionForAnimal (Restriction sup) {
    	if (sup.isAllValuesFromRestriction()) {
            return displayRestrictionForAnimal(sup.getOnProperty(), sup.asAllValuesFromRestriction().getAllValuesFrom() );
        }
        else if (sup.isSomeValuesFromRestriction()) {
            return displayRestrictionForAnimal(sup.getOnProperty(), sup.asSomeValuesFromRestriction().getSomeValuesFrom());
        }
        else {
        	return "";
        }
    }
    static String displayRestrictionForAnimal ( OntProperty onP, Resource constraint ) {
    	if (onP.toString().contains("HasHabitat") || onP.toString().contains("CanMove") || onP.toString().contains("Eat") ||
    			onP.toString().contains("HasLocation") || onP.toString().contains("HasBodyCover") ||
    			onP.toString().contains("HasBodypart"))
    	{
    		return "" + renderConstraint( constraint );
    	}
    	else {return "";}
    }
    
    
    protected static void displayType( OntClass sup ) {
        if (sup.isRestriction()) {
            displayRestriction( sup.asRestriction() );
        }
    }

    protected static void displayRestriction( Restriction sup ) {
        if (sup.isAllValuesFromRestriction()) {
            displayRestriction( "all", sup.getOnProperty(), sup.asAllValuesFromRestriction().getAllValuesFrom() );
        }
        else if (sup.isSomeValuesFromRestriction()) {
            displayRestriction( "some", sup.getOnProperty(), sup.asSomeValuesFromRestriction().getSomeValuesFrom());
        }

    }

    protected static void displayRestriction( String qualifier, OntProperty onP, Resource constraint ) {
        String out = String.format( "%s %s %s", qualifier, renderURI( onP ), renderConstraint( constraint ) );
        System.out.println( "animals : " + out );
    }

    protected static Object renderConstraint( Resource constraint ) {
        if (constraint.canAs( UnionClass.class )) {
            UnionClass uc = constraint.as( UnionClass.class );
            // this would be so much easier in ruby ...
            String r = "union{ ";
            for (Iterator<? extends OntClass> i = uc.listOperands(); i.hasNext(); ) {
                r = r + " " + renderURI( i.next() );
            }
            return r + "}";
        }
        else {
            return renderURI( constraint );
        }
    }

    protected static Object renderURI( Resource onP ) {
    	try {
    		String qName = onP.getModel().qnameFor( onP.getURI() );
    		return qName == null ? onP.getLocalName() : qName;
    	}
    	catch (NullPointerException e) {
    	 return null;	
    	}
    }


}
