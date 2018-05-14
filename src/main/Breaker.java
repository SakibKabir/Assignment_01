package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Breaker {

	private String rdfID;
	private String rdfIDSSH;
	private String name;
	private boolean state;
	private String equipmentContainer_rdfID;
	//private String baseVoltage_rdfID;

	// --------Return rdfID------//
	public String rdfID(Node node) {
		search(node);
		return rdfID;
	}

	// -------Return rdfIDSSH----//
	public String rdfIDSSH(Node node) {
		searchSSH(node);
		return rdfIDSSH;
	}

	// --------Return name-------//
	public String name(Node node) {
		search(node);
		return name;
	}

	// --------Return r------//
	public boolean state(Node node) {
		searchSSH(node);
		return state;
	}

	// --------Return Transformer_rdfID------//
	public String equipmentContainer_rdfID(Node node) {
		search(node);
		return equipmentContainer_rdfID;
	}

	/*
	 * // --------Return baseVoltage_rdfID-------// public String
	 * baseVoltage_rdfID(Node node) { search(node); return baseVoltage_rdfID; }
	 */

	// ---------Search the node----//
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		this.equipmentContainer_rdfID = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0)
				.getAttributes().item(0).getTextContent().replaceAll("#", "");
		// this.baseVoltage_rdfID =
		// element.getElementsByTagName("cim:TransformerEnd.BaseVoltage").item(0).getAttributes().item(0).getTextContent();
		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
		// "region_rdf:ID: " + region_rdfID + "\n");
		return element;
	}

	public Element searchSSH(Node node) {
		Element element = (Element) node;
		this.rdfIDSSH = element.getAttribute("rdf:about").replaceAll("#", "");
		this.state = Boolean.parseBoolean(element.getElementsByTagName("cim:Switch.open").item(0).getTextContent());
		return element;
	}
}