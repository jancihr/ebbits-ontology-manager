package eu.om.generator;

import java.util.*;

import org.openrdf.query.BindingSet;

import eu.om.ontology.Graph;
import eu.om.ontology.OrderComparator;
import eu.om.ontology.model.DigitalThingProperty;
import eu.om.ontology.model.EventServiceGrounding;
import eu.om.ontology.model.Namespace;
import eu.om.ontology.model.Rdf;
import eu.om.ontology.model.Service;
import eu.om.ontology.model.ServiceGrounding;
import eu.om.ontology.model.ServiceParameter;
import eu.om.ontology.model.ServiceParameterIterator;
import eu.om.ontology.model.ServicePrototype;
import eu.om.ontology.model.Thing;
import eu.om.renderer.LinkRenderer;

public class ServiceGenerator extends Generator {
	

	public String parameterMap(Set<Graph> params, String tagName, int indent) {
		String xml = indent("<"+tagName+"-parameters>", indent);
		List<Graph> sorted = new ArrayList<Graph>(params);
		Collections.sort(sorted, new OrderComparator());
		for(Graph param : sorted){
			xml += indent("<"+tagName+"-parameter>", (indent + 1));
			xml += indent("   <data-name>"+param.value(ServiceParameter.parameterName)+"</data-name>", (indent + 2));
			Graph reference = param.subGraph(ServiceParameter.parameterReference);
			if(reference != null){
				xml += indent("   <ebbits-name>"+reference.value(DigitalThingProperty.propertyName)+"</ebbits-name>", (indent + 2));
			}
			xml += indent("   <parameter-order>"+param.value(Thing.order)+"</parameter-order>", (indent + 2));
			xml += indent("</"+tagName+"-parameter>", (indent + 1));
		}
		xml += indent("</"+tagName+"-parameters>", indent);
		return xml;
	}

	public String iterator(Graph i, int indent) {
		String xml = indent("<output-parameter-iterator>", indent);
		String xpath = i.value(ServiceParameterIterator.elementXPathExpression);
		if(xpath != null){
			xml += indent("<iterator-element-xpath-expression>" + xpath + "</iterator-element-xpath-expression>", (indent + 1));
		}
		Graph ref = i.subGraph(ServiceParameterIterator.iterationElementReffersTo);
		if(ref != null){
			xml += indent("<iterator-element-reffers-to>" + Namespace.uriToPrefixedForm(ref.getBaseURI()) + "</iterator-element-reffers-to>", (indent + 1));
		}
		xml += parameterMap(i.subGraphs(ServiceParameterIterator.iterationParameters), "output", (indent + 1));
		xml += indent("</output-parameter-iterator>", indent);
		return xml;
	}
	
	public String iterators(Set<Graph> is, int indent) {
		String xml = indent("<output-parameter-iterators>", indent);
		List<Graph> sorted = new ArrayList<Graph>(is);
		Collections.sort(sorted, new OrderComparator());
		for(Graph i : sorted){
			xml += iterator(i, (indent + 1));
		}
		xml += indent("</output-parameter-iterators>", indent);
		return xml;
	}
	
	public String serviceReference(Graph s){
//		System.out.println(s.describe());
		Graph proto = s.subGraph(Service.hasPrototype);
//		System.out.println("SERVICE ["+s.getBaseURI()+"] PROTO: ["+proto.getBaseURI()+"]");
		if(proto != null){
			Graph ref = proto.subGraph(ServicePrototype.reffersTo);
//			System.out.println("PROTO: ["+proto.getBaseURI()+"] REF ["+ref.getBaseURI()+"]");
			if(ref != null){
				return Namespace.uriToPrefixedForm(ref.getBaseURI());
			}
		}
		return null;
	}

	public String serviceGrounding(Graph s, int indent){
		Graph g = s.subGraph(Service.serviceGrounding);
		String xml = indent("<service-grounding>", indent);

		if(g != null){
			if(g.value(Rdf.rdfType).equals(EventServiceGrounding.className.stringValue())){
				xml += indent("<event-topic>"+g.value(EventServiceGrounding.eventGroundingTopic)+"</event-topic>", (indent + 1));
			}
			else{
				xml += indent("<grounding-endpoint>"+g.value(ServiceGrounding.groundingEndpoint)+"</grounding-endpoint>", (indent + 1));
				xml += indent("<grounding-method>"+g.value(ServiceGrounding.groundingMethod)+"</grounding-method>", (indent + 1));
				xml += indent("<grounding-protocol>"+g.value(ServiceGrounding.groundingProtocol)+"</grounding-protocol>", (indent + 1));
				xml += indent("<grounding-output-format>"+g.value(ServiceGrounding.groundingOutputFormat)+"</grounding-output-format>", (indent + 1));
			}
		}
		xml += indent("</service-grounding>", indent);
		return xml;
	}
	
	public String endpointCall(Graph s, int indent){
		Graph renderer = s.subGraph(Service.endpointRenderer);
		Graph grounding = s.subGraph(Service.serviceGrounding);
		List<Graph> params = new ArrayList<Graph>(s.subGraphs(Service.serviceInput));
		Collections.sort(params, new OrderComparator());
		if(renderer != null && grounding != null){
			LinkRenderer link = new LinkRenderer();
			return indent("<endpoint-call>"+link.render(renderer, grounding, params)+"</endpoint-call>", indent);
		}
		return "";
	}
	class ServiceOutputComparator implements Comparator<Graph>{
		@Override
	    public int compare(Graph s1, Graph s2) {
			int o1 = s1.subGraphs(Service.serviceOutput).size();
			int o2 = s2.subGraphs(Service.serviceOutput).size();
	        return (o1 > o2 ? -1 : (o1 == o2 ? 0 : 1));
	    }
	}
	public String matchedServicesXML(Set<Graph> services) {
		List<Graph> list = new ArrayList<Graph>(services);
		Collections.sort(list, new ServiceOutputComparator());
		String xml = indent("<services>", 0);
		for(Graph s : list){
			
			xml += indent("<service>", 1);
			xml += indent("<service-type>"+s.rdfType()+"</service-type>", 2);
			xml += indent("<service-reference>"+serviceReference(s)+"</service-reference>", 2);
			xml += endpointCall(s, 2);
			xml += serviceGrounding(s, 2);
			xml += parameterMap(s.subGraphs(Service.serviceInput), "input", 2);
			xml += parameterMap(s.subGraphs(Service.serviceOutput), "output", 2);
			xml += iterators(s.subGraphs(Service.serviceOutputIterator), 2);
			xml += indent("</service>", 1);
		}
		xml += indent("</services>", 0);

		return xml;
	}
}
