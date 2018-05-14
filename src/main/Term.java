package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Term {
	
	private String rdfID, termEqui_rdfID, termNode_rdfID;
	
	// --------Return rdfID------//
	public String rdfID(Node node) {
		search(node);
		return rdfID;
	}
		
	// --------Return rdfID------//
	public String termEqui_rdfID(Node node) {
		search(node);
		return termEqui_rdfID;
	}	
		
	// --------Return rdfID------//
	public String termNode_rdfID(Node node) {
		search(node);
		return termNode_rdfID;
	}	
	
	// ---------Search the node----//
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");
		this.termEqui_rdfID = element.getElementsByTagName("cim:Terminal.ConductingEquipment").item(0)
				.getAttributes().item(0).getTextContent().replaceAll("#", "");
		this.termNode_rdfID = element.getElementsByTagName("cim:Terminal.ConnectivityNode").item(0)
				.getAttributes().item(0).getTextContent().replaceAll("#", "");
		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
		// "region_rdf:ID: " + region_rdfID + "\n");
		return element;
	}				
}
