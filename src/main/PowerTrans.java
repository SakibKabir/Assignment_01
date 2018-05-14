package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PowerTrans {
	private String rdfID;
	private String name;
	private String equipConta_rdfID;

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

	// --------Return r------//
	public String equipConta_rdfID(Node node) {
		search(node);
		return equipConta_rdfID;
	}

	// ---------Search the node----//
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		this.equipConta_rdfID = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0).getAttributes()
				.item(0).getTextContent().replaceAll("#", "");
		return element;
	}
}