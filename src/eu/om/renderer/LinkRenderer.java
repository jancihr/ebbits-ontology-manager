package eu.om.renderer;

import java.util.*;

import javax.xml.ws.EndpointReference;

import org.apache.commons.lang.StringUtils;

import eu.om.ontology.Graph;
import eu.om.ontology.model.DigitalThingProperty;
import eu.om.ontology.model.EndpointRenderer;
import eu.om.ontology.model.Namespace;
import eu.om.ontology.model.ServiceGrounding;
import eu.om.ontology.model.ServiceParameter;

public class LinkRenderer {
	public String render(Graph renderer, Graph grounding, List<Graph> parameters) {
		String link = grounding.value(ServiceGrounding.groundingEndpoint);
		if(parameters.size() > 0){
			Map<String, String> params = new HashMap<String, String>();
			for(Graph p : parameters){
				System.out.println("HANDLING PARAMETER:: ");
				System.out.println(p.describe());
				Graph ebbitsParam = p.subGraph(ServiceParameter.parameterReference);
				String paramName = p.value(ServiceParameter.parameterName);
				String ebbitsName = paramName;
				if(ebbitsParam != null){
					ebbitsName = ebbitsParam.value(DigitalThingProperty.propertyName);
				}
				params.put(ebbitsName, paramName);
			}
			System.err.println("params:: "+params);
			List<String> paramString = new ArrayList<String>();
			Iterator<String> i = params.keySet().iterator();
			while(i.hasNext()){
				String ebbitsName = i.next();
				String callName = params.get(ebbitsName);
				if(Namespace.uriToPrefixedForm(renderer.getBaseURI()).equals("ebbits:slashRenderer")){
					paramString.add("${" + ebbitsName + "}"); 
				}
				else if(Namespace.uriToPrefixedForm(renderer.getBaseURI()).equals("ebbits:andRenderer")){
					paramString.add(callName + "=${" + ebbitsName + "}"); 
				}
				else {
					String wrap = renderer.value(EndpointRenderer.parameterWrap);
					if(wrap != null){
						paramString.add(wrap + "${" + ebbitsName + "}" + wrap); 
					}
					else {
						paramString.add("${" + ebbitsName + "}"); 
					}
				}
			}
			if(Namespace.uriToPrefixedForm(renderer.getBaseURI()).equals("ebbits:slashRenderer")){
				link += StringUtils.join(paramString, "/");
			}
			else if(Namespace.uriToPrefixedForm(renderer.getBaseURI()).equals("ebbits:andRenderer")){
				link += "?" + StringUtils.join(paramString, "&amp;");
			}
			else {
				link += "/" + StringUtils.join(paramString, "");
			}
			return link;
			
		}
		return "";
	}
}
