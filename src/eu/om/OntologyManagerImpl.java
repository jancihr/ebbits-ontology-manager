package eu.om;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.rio.RDFFormat;

import eu.om.ontology.Graph;
import eu.om.ontology.model.Namespace;
import eu.om.ontology.model.Rdf;
import eu.om.repository.OMRepository;
import eu.om.repository.RepositoryFactory;
import eu.om.serializer.HTMLSerializer;

@WebService(
		endpointInterface = "eu.om.OntologyManager", 
		serviceName = "OntologyManager")
public class OntologyManagerImpl {
	
	public OMRepository repository = null;

	public OntologyManagerImpl(){
		System.out.println("starting OM IMPL");
		if(repository == null){
			this.repository = RepositoryFactory.owlim(System.getProperty("repository.storage.path"));
			System.out.println("\n\n===REPO CREATED!!===\n\n");
		}
	}

	public String match(String serviceType, String entityType){
		return this.repository.match(serviceType, entityType);
	}

    public String sparqlXML(String query) throws Exception {
        return this.repository.sparqlXML(query,  false);
    }

    public String sparqlXMLWithPreffixes(String query) throws Exception {
        return this.repository.sparqlXML(query,  true);
    }

	public void load() {
		System.out.println("loading repository");
		this.repository.load();
		this.repository.reloadPrefixMap();
	}

	public void clear() {
		System.out.println("clearing repository");
		this.repository.clear();
	}


	public void list() {
		this.repository.list();
	}
}
