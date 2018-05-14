package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

//---SEARCH BASEVOLTAGE----//
public class baseVoltage {

	private String rdfID;
	private double nominalVoltage;

	// --------Return rdfID------//
	public String rdfID(Node node) {
		search(node);
		return rdfID;
	}

	// ---Return nominalVoltage--//
	public double nominalVoltage(Node node) {
		search(node);
		return nominalVoltage;
	}

	// ---Load text of node------//
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");
		this.nominalVoltage = Double.parseDouble(element.getElementsByTagName("cim:BaseVoltage.nominalVoltage").item(0).getTextContent());
		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n");
		return element;
	}
	
}