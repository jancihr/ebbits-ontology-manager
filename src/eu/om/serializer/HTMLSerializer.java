package eu.om.serializer;

import java.util.HashSet;
import java.util.Iterator;

import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;

import eu.om.ontology.Graph;
import eu.om.ontology.model.Namespace;
import eu.om.ontology.model.Rdf;

public class HTMLSerializer {
	private String indent(int indent, String cnt) {
		String out = "";
		for(int i = 0; i < indent; i++){
			out += "  ";
		}
		return out + cnt + "\n";
	}
	
	public String link(String uri, String name) {
		return "<a href=\"javascript:void(0)\" onClick=\"Data.toggle('"+Namespace.uriToPrefixedForm(uri, "-")+"')\">" + name + "</a>";
	}

	public String graph2HTML(Graph g, Value base, int indent, HashSet<String> used){
		String out = "";
		if(base instanceof Resource && !used.contains(base.stringValue())){
			used.add(base.stringValue());
			URI baseURI = new URIImpl(base.stringValue());
			String basePrefix = Namespace.prefix(baseURI.getNamespace());
			String baseValue = "";
			if(basePrefix == null){
				baseValue = baseURI.stringValue();
			}
			else{
				baseValue =  basePrefix + ":" + baseURI.getLocalName();
			}
			Iterator<Statement> i = g.match((Resource)base, null, null);
			boolean hasProps = i.hasNext();
			if(hasProps) out += indent(indent, "<ul class=\"props\" id=\""+Namespace.uriToPrefixedForm(base.stringValue(), "-")+"\">");
			while(i.hasNext()){
				Statement s = i.next();
				Value obj = s.getObject();
				String value = obj.stringValue();
				if(obj instanceof Resource) {
					URI objURI = new URIImpl(obj.stringValue());
					String prefix = Namespace.prefix(objURI.getNamespace());

					if(prefix == null){
						value = link(obj.stringValue(), obj.stringValue());
					}
					else{
						value =  link(objURI.stringValue(), prefix + ":" + objURI.getLocalName());
					}
				}
				if(obj instanceof Literal){
					Literal literal = (Literal)obj;
					URI dt = literal.getDatatype();
					value = "\"" + 
					literal.stringValue() + 
					"\" type of " + 
					Namespace.prefix(dt.getNamespace()) + ":" + 
					dt.getLocalName();
				}
				URI p = s.getPredicate();

				out += indent((indent + 2), "<li>" + (Namespace.prefix(p.getNamespace()) + ":" + p.getLocalName() + ": " + value) + "</li>");
				if(!p.stringValue().equals(Rdf.rdfType.stringValue())){
					out += graph2HTML(g, s.getObject(), (indent + 4), used);
				}
			}
			if(hasProps) out += indent(indent, "</ul>");
		}
		return out;

	}

	public String graph2HTML(Graph g){
		String html = graph2HTML(g, new URIImpl(g.getBaseURI()), 2, new HashSet<String>());
		String value = "";
		URI objURI = new URIImpl(g.getBaseURI());
		String prefix = Namespace.prefix(objURI.getNamespace());

		if(prefix == null){
			value = link(g.getBaseURI(), g.getBaseURI());
		}
		else{
			value =  link(objURI.stringValue(), prefix + ":" + objURI.getLocalName());
		}

		html = indent(0, "<li>" + value) + html + indent(0, "</li>");
		return html;
	}
}
