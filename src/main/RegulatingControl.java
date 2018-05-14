package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class RegulatingControl {
	
	private String rdfIDEQ, rdfIDSSH;
	private String name;
	private double targetValue;
	
		// --------Return rdfID------//
		public String rdfIDEQ(Node node) {
			searchEQ(node);
			return rdfIDEQ;
		}

		// --------Return name-------//
		public String name(Node node) {
			searchEQ(node);
			return name;
		}
		
		
		// --------Return rdfID------//
		public String rdfIDSSH(Node node) {
			searchSSH(node);
			return rdfIDSSH;
		}
				
		// ------Return targetValue------//
		public double targetValue(Node node) {
			searchSSH(node);
			return targetValue;
		}
		
		public Element searchEQ(Node node) {
			Element element = (Element) node;
			this.rdfIDEQ = element.getAttribute("rdf:ID").replaceAll("#","");
			this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
			return element;
		}
		
		public Element searchSSH(Node node) {
			Element element = (Element) node;
			this.rdfIDSSH = element.getAttribute("rdf:about").replaceAll("#","");
			this.targetValue = Double.parseDouble(element.getElementsByTagName("cim:RegulatingControl.targetValue").item(0).getTextContent());
			// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
			// "region_rdf:ID: " + region_rdfID + "\n");
			return element;
		}


}