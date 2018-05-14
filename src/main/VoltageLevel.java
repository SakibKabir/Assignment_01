package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VoltageLevel {

	private String rdfID;
	private String name;
	private String substation_rdfID;
	private String baseVoltage_rdfID;

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

	// -Return substation_rdfID- //
	public String substation_rdfID(Node node) {
		search(node);
		return substation_rdfID;
	}
	
	// -Return substation_rdfID- //
	public String baseVoltage_rdfID(Node node) {
		search(node);
		return baseVoltage_rdfID;
	}
	
	// ---Load text of node------//
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		this.substation_rdfID = element.getElementsByTagName("cim:VoltageLevel.Substation").item(0).getAttributes().item(0).getTextContent().replaceAll("#", "");
		this.baseVoltage_rdfID = element.getElementsByTagName("cim:VoltageLevel.BaseVoltage").item(0).getAttributes().item(0).getTextContent().replaceAll("#", "");
		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
		// "region_rdf:ID: " + region_rdfID + "\n");
		return element;
	}

}