package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PowerTransformerEnd {
	private String rdfID;
	private String name;
	private double r, x, ratedU, b, g;
	private String Transformer_rdfID;
	private String baseVoltage_rdfID;
	private String terminal_rdfID;

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
	public double r(Node node) {
		search(node);
		return r;
	}

	// --------Return x-------//
	public double x(Node node) {
		search(node);
		return x;
	}

	// --------Return b------//
	public double b(Node node) {
		search(node);
		return b;
	}

	// --------Return g-------//
	public double g(Node node) {
		search(node);
		return g;
	}

	// --------Return ratedU-------//
	public double ratedU(Node node) {
		search(node);
		return ratedU;
	}

	// --------Return Transformer_rdfID------//
	public String Transformer_rdfID(Node node) {
		search(node);
		return Transformer_rdfID;
	}

	// --------Return baseVoltage_rdfID-------//
	public String baseVoltage_rdfID(Node node) {
		search(node);
		return baseVoltage_rdfID;
	}

	// --------Return terminal rdfID------//
	public String terminal_rdfID(Node node) {
		search(node);
		return terminal_rdfID;
	}

	// ---------Search the node----//
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();

		this.r = Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.r").item(0).getTextContent());
		this.x = Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.x").item(0).getTextContent());
		this.b = Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.b").item(0).getTextContent());
		this.g = Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.g").item(0).getTextContent());
		this.ratedU = Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.ratedU").item(0).getTextContent());

		this.Transformer_rdfID = element.getElementsByTagName("cim:PowerTransformerEnd.PowerTransformer").item(0)
				.getAttributes().item(0).getTextContent().replaceAll("#", "");
		this.baseVoltage_rdfID = element.getElementsByTagName("cim:TransformerEnd.BaseVoltage").item(0).getAttributes()
				.item(0).getTextContent().replaceAll("#", "");
		this.terminal_rdfID = element.getElementsByTagName("cim:TransformerEnd.Terminal").item(0).getAttributes()
				.item(0).getTextContent().replaceAll("#", "");

		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
		// "region_rdf:ID: " + region_rdfID + "\n");
		return element;
	}
}