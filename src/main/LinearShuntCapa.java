package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class LinearShuntCapa {

	private String rdfID, rdfIDSSH;
	double nomU, b, g, sectionnumber;
	double[] bgU = new double[3];

	// --------Return rdfID------//
	public String rdfID(Node node) {
		search(node);
		return rdfID;
	}

	// --------Return rdfIDSSH------//
	public String rdfIDSSH(Node node) {
		searchSSH(node);
		return rdfIDSSH;
	}

	// --------Return bgU------//
	public double[] bgU(Node node) {
		search(node);
		return bgU;
	}

	// --------Return sectionnumber------//
	public double sectionnumber(Node node) {
		searchSSH(node);
		return sectionnumber;
	}

	// ---------Search the node----//
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");

		this.b = Double.parseDouble(
				element.getElementsByTagName("cim:LinearShuntCompensator.bPerSection").item(0).getTextContent());
		this.g = Double.parseDouble(
				element.getElementsByTagName("cim:LinearShuntCompensator.gPerSection").item(0).getTextContent());
		this.nomU = Double
				.parseDouble(element.getElementsByTagName("cim:ShuntCompensator.nomU").item(0).getTextContent());

		bgU[0] = b;
		bgU[1] = g;
		bgU[2] = nomU;
		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
		// "region_rdf:ID: " + region_rdfID + "\n");
		return element;
	}

	public Element searchSSH(Node node) {
		Element element = (Element) node;
		this.rdfIDSSH = element.getAttribute("rdf:about").replaceAll("#", "");

		this.sectionnumber = Double
				.parseDouble(element.getElementsByTagName("cim:ShuntCompensator.sections").item(0).getTextContent());
		return element;
	}

}