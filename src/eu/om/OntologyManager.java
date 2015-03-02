package eu.om;

import javax.jws.WebService;

@WebService
public interface OntologyManager {
	/**
	 * Generic sparql query interface
	 * @param query SPARQL query
	 * @return Result XML
	 */
    public String sparqlXML(String query);
    
	/**
	 * Generic sparql query interface
	 * @param query SPARQL query
	 * @return Result XML
	 */
    public String sparqlXMLWithPreffixes(String query);

	/**
	 * Returns most suitable service model with whole grounding information
	 * example: match("ebbits:BasicDataService", "ebbits:cow")
	 * @param serviceType The taxonomy service type 
	 * @param entityType Instance of entity to which the service is related 
	 * @return XML containing service description
	 */
	public String match(String serviceType, String entityType);

	
	/**
	 * Clears semantic storage 
	 */
	public boolean clear();

	/**
	 * Loads semantic storage 
	 */
	public boolean load();

}

