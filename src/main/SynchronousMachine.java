package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
//import org.w3c.dom.NamedNodeMap;
//import java.lang.*;

public class SynchronousMachine {

	private String rdfID;
	private String name;
	private double ratedS;
	private double P;
	private double Q;
	private String genUnit_rdfID;
	private String regControl_rdfID;
	private String equipmentContainer_rdfID;
	private double baseVoltage;

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
	
	// ------Return ratedS------//
	public double ratedS(Node node) {
		search(node);
		return ratedS;
	}
	
	// ------Return P------//
	public double P(Node node) {
		search(node);
		return P;
	}
	
	// ------Return Q------//
	public double Q(Node node) {
		search(node);
		return Q;
	}
	
	// -Return genUnit_rdfID- //
	public String genUnit_rdfID(Node node) {
		search(node);
		return genUnit_rdfID;
	}
	
	// -Return regControl_rdfID- //
	public String regControl_rdfID(Node node) {
		search(node);
		return regControl_rdfID;
	}
		
	// -Return equipmentContainer_rdfID- //
	public String equipmentContainer_rdfID(Node node) {
		search(node);
		return equipmentContainer_rdfID;
	}
	
	// -Return baseVoltage- //
	public double baseVoltage(Node node) {
		search(node);
		return baseVoltage;
	}

	// ---Load text of node------//
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		
		this.ratedS = Double.parseDouble(element.getElementsByTagName("cim:RotatingMachine.ratedS").item(0).getTextContent());
		double powerFactor =  Double.parseDouble(element.getElementsByTagName("cim:RotatingMachine.ratedPowerFactor").item(0).getTextContent());
		this.P = ratedS*powerFactor;
		this.Q = ratedS*Math.sqrt(1-powerFactor*powerFactor);

		this.genUnit_rdfID = element.getElementsByTagName("cim:RotatingMachine.GeneratingUnit").item(0).getAttributes().item(0).getTextContent().replaceAll("#", "");
		this.regControl_rdfID = element.getElementsByTagName("cim:RegulatingCondEq.RegulatingControl").item(0).getAttributes().item(0).getTextContent().replaceAll("#", "");
		this.equipmentContainer_rdfID = element.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0).getAttributes().item(0).getTextContent().replaceAll("#", "");
		this.baseVoltage =  Double.parseDouble(element.getElementsByTagName("cim:RotatingMachine.ratedU").item(0).getTextContent());
		
		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
		// "region_rdf:ID: " + region_rdfID + "\n");
		return element;
	}
	


}