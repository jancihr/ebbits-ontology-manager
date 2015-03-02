package eu.om.generator;

public class Generator {
	protected String indent(String cnt, int indent) {
		String out = "";
		for(int i = 0; i < indent; i++){
			out += "  ";
		}
		return out + cnt + "\n";
//		return cnt.trim();
	}

   

}
