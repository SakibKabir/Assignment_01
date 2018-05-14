package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ACLine {

	private String rdfID, basevoltage_rdfID;
	private double[] rxgbl = new double[5];
	private double r, x, g, b, length;

	// --------Return rdfID------//
	public String rdfID(Node node) {
		search(node);
		return rdfID;
	}

	// --------Return rdfID------//
	public String basevoltage_rdfID(Node node) {
		search(node);
		return basevoltage_rdfID;
	}

	// --------Return r, x, g, b, length------//
	public double[] rxgbl(Node node) {
		search(node);
		rxgbl[0] = r;
		rxgbl[1] = x;
		rxgbl[2] = g;
		rxgbl[3] = b;
		rxgbl[4] = length;
		return rxgbl;
	}

	// ---------Search the node----//
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");
		this.basevoltage_rdfID = element.getElementsByTagName("cim:ConductingEquipment.BaseVoltage").item(0)
				.getAttributes().item(0).getTextContent().replaceAll("#", "");
		this.r = Double.parseDouble(element.getElementsByTagName("cim:ACLineSegment.r").item(0).getTextContent());
		this.x = Double.parseDouble(element.getElementsByTagName("cim:ACLineSegment.x").item(0).getTextContent());
		this.b = Double.parseDouble(element.getElementsByTagName("cim:ACLineSegment.bch").item(0).getTextContent());
		this.g = Double.parseDouble(element.getElementsByTagName("cim:ACLineSegment.gch").item(0).getTextContent());
		this.length = Double.parseDouble(element.getElementsByTagName("cim:Conductor.length").item(0).getTextContent());

		// element.getElementsByTagName("cim:TransformerEnd.BaseVoltage").item(0).getAttributes().item(0).getTextContent();
		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
		// "region_rdf:ID: " + region_rdfID + "\n");
		return element;
	}

}
