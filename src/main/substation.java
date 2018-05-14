package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

//---SEARCH SUBSTATION----//
public class substation {

	private String rdfID;
	private String name;
	private String region_rdfID;

	// --------Return rdfID------//
	public String rdfID(Node node) {
		search(node);
		return rdfID;
	}

	// --------Return name-------//
	public String name(Node node) {
		search(node);
		return name;
	}

	// ---Return region_rdfID---//
	public String region_rdfID(Node node) {
		search(node);
		return region_rdfID;
	}

	// ---Load text of node------//
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		this.region_rdfID = element.getElementsByTagName("cim:Substation.Region").item(0).getAttributes().item(0)
				.getTextContent().replaceAll("#", "");
		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
		// "region_rdf:ID: " + region_rdfID + "\n");
		return element;
	}

}