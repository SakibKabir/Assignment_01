package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class EnergyConsumer {
	private String rdfID, rdfIDSSH;
	private String name;
	private double P;
	private double Q;
	private String equipmentContainer_rdfID;
	private String baseVoltage_rdfID;

	// --------Return rdfID------//
	public String rdfID(Node node) {
		search(node);
		return rdfID;
	}

	// --------Return rdfID------//
	public String rdfIDSSH(Node node) {
		searchSSH(node);
		return rdfIDSSH;
	}

	// --------Return name-------//
	public String name(Node node) {
		search(node);
		return name;
	}

	// --------Return P------//
	public double P(Node node) {
		searchSSH(node);
		return P;
	}

	// --------Return Q-------//
	public double Q(Node node) {
		searchSSH(node);
		return Q;
	}

	// --------Return Transformer_rdfID------//
	public String equipmentContainer_rdfID(Node node) {
		search(node);
		return equipmentContainer_rdfID;
	}

	// --------Return baseVoltage_rdfID-------//
	public String baseVoltage_rdfID(Node node) {
		search(node);
		return baseVoltage_rdfID;
	}

	// ---------Search the node----//
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();

		this.equipmentContainer_rdfID = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0)
				.getAttributes().item(0).getTextContent().replaceAll("#","");
		//this.baseVoltage_rdfID = element.getElementsByTagName("cim:TransformerEnd.BaseVoltage").item(0).getAttributes()
		//		.item(0).getTextContent();
		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
		// "region_rdf:ID: " + region_rdfID + "\n");
		return element;
	}

	// ---------Search the node----//
	public Element searchSSH(Node node) {
		Element element = (Element) node;
		this.rdfIDSSH = element.getAttribute("rdf:about").replaceAll("#", "");
		this.P = Double.parseDouble(element.getElementsByTagName("cim:EnergyConsumer.p").item(0).getTextContent());
		this.Q = Double.parseDouble(element.getElementsByTagName("cim:EnergyConsumer.q").item(0).getTextContent());
		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
		// "region_rdf:ID: " + region_rdfID + "\n");
		return element;
	}

}