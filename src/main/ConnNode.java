package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ConnNode {

	private String rdfID;
	private String conatiner_rdfID;

	// --------Return rdfID------//
	public String rdfID(Node node) {
		search(node);
		return rdfID;
	}

	// --------Return container rdfID------//
	public String conatiner_rdfID(Node node) {
		search(node);
		return conatiner_rdfID;
	}

	// ---------Search the node----//
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");
		this.conatiner_rdfID = element.getElementsByTagName("cim:ConnectivityNode.ConnectivityNodeContainer").item(0).getAttributes().item(0)
				.getTextContent().replaceAll("#", "");
		
		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
		// "region_rdf:ID: " + region_rdfID + "\n");
		return element;
	}

}
