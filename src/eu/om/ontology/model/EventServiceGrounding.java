package eu.om.ontology.model;

import eu.om.ontology.schema.LiteralProperty;
import eu.om.ontology.schema.OntologyClass;

public class EventServiceGrounding {
	public final static OntologyClass className = 
		new OntologyClass(Namespace.ebbits + "EventServiceGrounding");

	
	public final static LiteralProperty eventGroundingTopic = 
		new LiteralProperty(Namespace.ebbits + "eventGroundingTopic");
}
