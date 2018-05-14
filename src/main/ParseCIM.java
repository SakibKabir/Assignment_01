package main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ParseCIM {
	//int[][] this.Termindex;
	public static int[][] Termindex ;
	public static int[][] Connindex ;
	public static List<String> Term_CondEqui_List = new ArrayList<String>(); ;
	public static NodeList TermList = null;
	public static NodeList ConnNodeList = null;
	public static NodeList BusbarSectList = null;
	public static double syncMach_ratedS;
	public double[][] LinearShuntCom_bgUs;
	public double[][] rxbglv;
	public double[][] PowetTransEnd_rxvgb;
	//int[][] this.Termindex = new int[this.TermList.getLength()][8];
	
	
 public void mainParsing(File EQ, File SSH) throws Exception {

		// File EQ = new File("MicroGridTestConfiguration_T1_BE_EQ_V2.xml");
		// File SSH = new File("MicroGridTestConfiguration_T1_BE_SSH_V2.xml");

		DocumentBuilderFactory dbFactoryEQ = DocumentBuilderFactory.newInstance();
		DocumentBuilderFactory dbFactorySSH = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilderEQ = dbFactoryEQ.newDocumentBuilder();
		DocumentBuilder dBuilderSSH = dbFactorySSH.newDocumentBuilder();
		Document docEQ = dBuilderEQ.parse(EQ);
		Document docSSH = dBuilderSSH.parse(SSH);
		docEQ.getDocumentElement().normalize();
		docSSH.getDocumentElement().normalize();
		System.out.println("Load Assignment_EQ_reduced.xml successfully");
		System.out.println("Load Assignment_SSH_reduced.xml successfully");

		// ------Create Node List for each objectives------//
		// -----------------From EQ file-------------------//
		NodeList baseVoltList = docEQ.getElementsByTagName("cim:BaseVoltage");
		NodeList subsList = docEQ.getElementsByTagName("cim:Substation");
		NodeList voltageLevelList = docEQ.getElementsByTagName("cim:VoltageLevel");
		NodeList generatingUnit = docEQ.getElementsByTagName("cim:GeneratingUnit");
		NodeList syncMachList = docEQ.getElementsByTagName("cim:SynchronousMachine");
		NodeList regulCtrlListEQ = docEQ.getElementsByTagName("cim:RegulatingControl");
		NodeList powerTranList = docEQ.getElementsByTagName("cim:PowerTransformer");
		NodeList energyConsumerList = docEQ.getElementsByTagName("cim:EnergyConsumer");
		NodeList powerTranEndList = docEQ.getElementsByTagName("cim:PowerTransformerEnd");
		NodeList breakerList = docEQ.getElementsByTagName("cim:Breaker");
		NodeList ratioTapChangerList = docEQ.getElementsByTagName("cim:RatioTapChanger");
		ConnNodeList = docEQ.getElementsByTagName("cim:ConnectivityNode");
		TermList = docEQ.getElementsByTagName("cim:Terminal");
		BusbarSectList = docEQ.getElementsByTagName("cim:BusbarSection");
		NodeList ACLineList = docEQ.getElementsByTagName("cim:ACLineSegment");
		NodeList LinearShuntCapaList = docEQ.getElementsByTagName("cim:LinearShuntCompensator");

		// ----------------From SSH file-------------------//
		NodeList baseVoltListSSH = docSSH.getElementsByTagName("cim:BaseVoltage");
		NodeList subsListSSH = docSSH.getElementsByTagName("cim:Substation");
		NodeList voltageLevelListSSH = docSSH.getElementsByTagName("cim:VoltageLevel");
		NodeList generatingUnitSSH = docSSH.getElementsByTagName("cim:GeneratingUnit");
		NodeList syncMachListSSH = docSSH.getElementsByTagName("cim:SynchronousMachine");
		NodeList regulCtrlListSSH = docSSH.getElementsByTagName("cim:RegulatingControl");
		NodeList powerTranListSSH = docSSH.getElementsByTagName("cim:PowerTransformer");
		NodeList energyConsumerListSSH = docSSH.getElementsByTagName("cim:EnergyConsumer");
		NodeList powerTranEndListSSH = docSSH.getElementsByTagName("cim:PowerTransformerEnd");
		NodeList breakerListSSH = docSSH.getElementsByTagName("cim:Breaker");
		NodeList ratioTapChangerListSSH = docSSH.getElementsByTagName("cim:RatioTapChanger");
		NodeList ConnNodeListSSH = docSSH.getElementsByTagName("cim:ConnectivityNode");
		NodeList TermListSSH = docSSH.getElementsByTagName("cim:Terminal");
		NodeList LinearShuntCapaListSSH = docSSH.getElementsByTagName("cim:LinearShuntCompensator");

		
		//SQLdatabase database = new SQLdatabase();
	//	database.StartUp();
		//database.createTables();
		// ------------------------------//
		// ---Load base voltage info ----//
		// -- rdfID, nominal Voltage ----//
		// ---create table: BaseVoltage--//
		// ------------------------------//
		baseVoltage baseVolt = new baseVoltage();
		List<String> basevoltage_rdfID_List = new ArrayList<String>();
		double[] basevoltage_List = new double[baseVoltList.getLength()];

		for (int i = 0; i < baseVoltList.getLength(); i++) {
			String baseVolt_rdfID = baseVolt.rdfID(baseVoltList.item(i));
			double baseVolt_nominalVoltage = (double) baseVolt.nominalVoltage(baseVoltList.item(i));
			basevoltage_rdfID_List.add(baseVolt_rdfID);
			basevoltage_List[i] = baseVolt_nominalVoltage;
			SQLdatabase.BaseVoltageTable(baseVolt_rdfID, baseVolt_nominalVoltage);
			System.out.println("BaseVoltage: rdfID: " + baseVolt_rdfID);
			System.out.println("             nominal Voltage: " + baseVolt_nominalVoltage + "kV");
		}

		// ------------------------------//
		// -----Load substation info-----//
		// --rdfID, name, region_rdfID---//
		// ---create table: Substation---//
		// ------------------------------//
		substation subs = new substation();
		for (int i = 0; i < subsList.getLength(); i++) {
			String subs_rdfID = subs.rdfID(subsList.item(i));
			String subs_name = subs.name(subsList.item(i));
			String subs_region_rdfID = subs.region_rdfID(subsList.item(i));
			SQLdatabase.SubstationTable(subs_rdfID, subs_name, subs_region_rdfID);
			System.out.println("Substation: rdfID: " + subs_rdfID);
			System.out.println("            name: " + subs_name);
			System.out.println("            region_rdfID: " + subs_region_rdfID);
		}

		// ------------------------------//
		// ---Load Voltage Level info----//
		// ---rdfID, name, maxP, minP----//
		// ---equipmentContainer_rdf:ID--//
		// --create table: VoltageLevel--//
		// ------------------------------//
		VoltageLevel voltageLevel = new VoltageLevel();
		for (int i = 0; i < voltageLevelList.getLength(); i++) {
			String voltageLevel_rdfID = voltageLevel.rdfID(voltageLevelList.item(i));
			String voltageLevel_name = voltageLevel.name(voltageLevelList.item(i));
			String voltageLevel_subs_rdfID = voltageLevel.substation_rdfID(voltageLevelList.item(i));
			String voltageLevel_baseVolt_rdfID = voltageLevel.baseVoltage_rdfID(voltageLevelList.item(i));
			SQLdatabase database1 = new SQLdatabase();
			database1.VoltageLevelTable(voltageLevel_rdfID, voltageLevel_name, voltageLevel_subs_rdfID,
					voltageLevel_baseVolt_rdfID);
			System.out.println("Voltage Level: rdfID: " + voltageLevel_rdfID);
			System.out.println("               name: " + voltageLevel_name);
			System.out.println("               substation rdfID: " + voltageLevel_subs_rdfID);
			System.out.println("               base voltage rdfID: " + voltageLevel_baseVolt_rdfID);
		}

		// ------------------------------//
		// --Load Generating Unit info---//
		// --rdfID, substation_rdf:ID----//
		// ----name,baseVoltage_rdf:ID---//
		// ----create table: GeneUnit----//
		// ------------------------------//
		GeneratingUnit genunit = new GeneratingUnit();
		for (int i = 0; i < generatingUnit.getLength(); i++) {
			String GeneUnit_rdfID = genunit.rdfID(generatingUnit.item(i));
			String GeneUnit_name = genunit.name(generatingUnit.item(i));
			double GeneUnit_maxP = genunit.maxP(generatingUnit.item(i));
			double GeneUnit_minP = genunit.minP(generatingUnit.item(i));
			String GeneUnit_equipCont_rdfID = genunit.equipmentContainer_rdfID(generatingUnit.item(i));
			SQLdatabase.GeneratingUnitTable(GeneUnit_rdfID, GeneUnit_name, GeneUnit_maxP, GeneUnit_minP,
					GeneUnit_equipCont_rdfID);
			System.out.println("Generating Unit: rdfID: " + GeneUnit_rdfID);
			System.out.println("                 name: " + GeneUnit_name);
			System.out.println("                 maxP: " + GeneUnit_maxP);
			System.out.println("                 minP: " + GeneUnit_minP);
			System.out.println("                 equipmentContainer_rdfID: " + GeneUnit_equipCont_rdfID);
		}
		// ------------------------------//

		// ------------------------------//
		// Load synchronous machine info-//
		// ----rdfID, name, ratedS, P----//
		// Q,genUnit_rdf:ID,regControl_rdf:ID//
		// --equipmentContainer_rdf:ID---//
		// ----baseVoltage_ rdf:ID-------//
		// ----create table: SyncMach----//
		// ------------------------------//
		List<String> Sync_rdfID_List = new ArrayList<String>();
		SynchronousMachine syncMach = new SynchronousMachine();
		syncMach_ratedS = 1e-5;
		for (int i = 0; i < syncMachList.getLength(); i++) {
			String syncMach_rdfID = syncMach.rdfID(syncMachList.item(i));
			String syncMach_name = syncMach.name(syncMachList.item(i));
			syncMach_ratedS = syncMach.ratedS(syncMachList.item(i));
			double syncMach_P = syncMach.P(syncMachList.item(i));
			double syncMach_Q = syncMach.Q(syncMachList.item(i));
			String syncMach_genUnit_rdfID = syncMach.genUnit_rdfID(syncMachList.item(i));
			String syncMach_regControl_rdfID = syncMach.regControl_rdfID(syncMachList.item(i));
			String syncMach_equipmentContainer_rdfID = syncMach.equipmentContainer_rdfID(syncMachList.item(i));
			double syncMach_ratedU = syncMach.baseVoltage(syncMachList.item(i));
			String syncMach_baseVoltage_rdfID = "Null";
			for (int j = 0; j < baseVoltList.getLength(); j++) {
				double baseVolt_nominalVoltage = (double) baseVolt.nominalVoltage(baseVoltList.item(j));
				String baseVolt_rdfID = baseVolt.rdfID(baseVoltList.item(j));
				if (syncMach_ratedU == baseVolt_nominalVoltage) {
					syncMach_baseVoltage_rdfID = baseVolt_rdfID;
				}
			}
			Sync_rdfID_List.add(syncMach_rdfID);

			SQLdatabase.SynchronousMachineTable(syncMach_rdfID, syncMach_name, syncMach_ratedS, syncMach_P,
					syncMach_Q, syncMach_genUnit_rdfID, syncMach_regControl_rdfID,
					syncMach_equipmentContainer_rdfID, syncMach_baseVoltage_rdfID);
			// SQLdatabase.SynchronousMachineTable(syncMach_rdfID, syncMach_name,
			// syncMach_ratedS, syncMach_P, syncMach_Q, syncMach_genUnit_rdfID,
			// syncMach_regControl_rdfID, syncMach_equipmentContainer_rdfID,
			// syncMach_baseVoltage_rdfID);

			System.out.println("Synchronous Machine: rdfID: " + syncMach_rdfID);
			System.out.println("                     name: " + syncMach_name);
			System.out.println("                     ratedS: " + syncMach_ratedS);
			System.out.println("                     P: " + syncMach_P);
			System.out.println("                     Q: " + syncMach_Q);
			System.out.println("                     genUnit rdfId: " + syncMach_genUnit_rdfID);
			System.out.println("                     regControl_rdfID: " + syncMach_regControl_rdfID);
			System.out.println(
					"                     equipmentContainer_rdf:ID: " + syncMach_equipmentContainer_rdfID);
			System.out.println("                     rated U: " + syncMach_ratedU);
			System.out.println("                     baseVoltage_ rdf:ID: " + syncMach_baseVoltage_rdfID);
			// SQLdatabase.SynchronousMachineTable(syncMach_rdfID, syncMach_name,
			// syncMach_ratedS, syncMach_P, syncMach_Q, syncMach_genUnit_rdfID,
			// syncMach_regControl_rdfID, syncMach_equipmentContainer_rdfID,
			// syncMach_baseVoltage_rdfID);
		}
		// -Load Regulating Control info-//
		// ---rdfID, name, targetValue---//
		// ----create table: ReguCtrl----//
		// ------------------------------//
		RegulatingControl reglCtrl = new RegulatingControl();
		String reglCtrl_rdfID_temp, reglCtrl_rdfID_tempSSH, reglCtrl_name;
		double reglCtrl_targetVoltage;
		for (int i = 0; i < regulCtrlListEQ.getLength(); i++) {
			reglCtrl_rdfID_temp = reglCtrl.rdfIDEQ(regulCtrlListEQ.item(i));
			for (int j = 0; j < regulCtrlListSSH.getLength(); j++) {
				reglCtrl_rdfID_tempSSH = reglCtrl.rdfIDSSH(regulCtrlListSSH.item(j));
				if (reglCtrl_rdfID_temp.equals(reglCtrl_rdfID_tempSSH)) {
					reglCtrl_name = reglCtrl.name(regulCtrlListEQ.item(i));
					reglCtrl_targetVoltage = reglCtrl.targetValue(regulCtrlListSSH.item(j));
					SQLdatabase.RegulatingControlTable(reglCtrl_rdfID_temp, reglCtrl_name, reglCtrl_targetVoltage);
					// System.out.println("RegulatingControl : rdfID: " + rdfID_tempSSH);
					System.out.println("RegulatingControl : rdfID: " + reglCtrl_rdfID_temp);
					System.out.println("                    name: " + reglCtrl_name);
					System.out.println("                    target voltage: " + reglCtrl_targetVoltage);
				}
			}

		}
		// -------------------------------------//
		// -----Load Power Transformer info-----//
		// -rdfID, name,quipmentContainer_rdf:ID//
		// ------create table: PowerTran--------//
		// -------------------------------------//
		PowerTrans powerTran = new PowerTrans();
		List<String> PowerTran_rdfID_List = new ArrayList<String>();
		for (int i = 0; i < powerTranList.getLength(); i++) {
			String powerTran_rdfID = powerTran.rdfID(powerTranList.item(i));
			String powerTran_name = powerTran.name(powerTranList.item(i));
			String powerTran_equipCont_rdfID = powerTran.equipConta_rdfID(powerTranList.item(i));
			PowerTran_rdfID_List.add(powerTran_rdfID);

			SQLdatabase.PowerTransformerTable(powerTran_rdfID, powerTran_name, powerTran_equipCont_rdfID);
			System.out.println("power Transformer: rdfID: " + powerTran_rdfID);
			System.out.println("                   name: " + powerTran_name);
			System.out.println("                   region_rdfID: " + powerTran_equipCont_rdfID);
		}
		System.out.println(" %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

		// -------------------------------------//
		// ----Load Energy Consumer info--------//
		// ------------rdfID, name, P, Q--------//
		// equipmentContainer_rdf:ID, baseVoltage_ rdf:ID//
		// ------create table: RatioTapChan-----//
		// -------------------------------------//
		EnergyConsumer energyConsu = new EnergyConsumer();
		String energyConsu_rdfID_temp, energyConsu_rdfID_tempSSH, energyConsu_name,
				energyConsu_equipmentContainer_rdfID, energyConsu_baseVoltage_rdfID = "Null";
		double energyConsu_P, energyConsu_Q;
		List<String> EnerCons_rdfID_List = new ArrayList<String>();
		for (int i = 0; i < energyConsumerList.getLength(); i++) {
			energyConsu_rdfID_temp = reglCtrl.rdfIDEQ(energyConsumerList.item(i));
			for (int j = 0; j < energyConsumerListSSH.getLength(); j++) {
				energyConsu_rdfID_tempSSH = energyConsu.rdfIDSSH(energyConsumerListSSH.item(j));
				if (energyConsu_rdfID_temp.equals(energyConsu_rdfID_tempSSH)) {
					energyConsu_name = energyConsu.name(energyConsumerList.item(i));
					energyConsu_P = energyConsu.P(energyConsumerListSSH.item(j));
					energyConsu_Q = energyConsu.Q(energyConsumerListSSH.item(j));
					energyConsu_equipmentContainer_rdfID = energyConsu
							.equipmentContainer_rdfID(energyConsumerList.item(i));

					VoltageLevel volt = new VoltageLevel();
					for (int k = 0; k < voltageLevelList.getLength(); k++) {
						String volytageLevel_rdfID = volt.rdfID(voltageLevelList.item(k));
						if (energyConsu_equipmentContainer_rdfID.equals(volytageLevel_rdfID)) {
							energyConsu_baseVoltage_rdfID = volt.baseVoltage_rdfID(voltageLevelList.item(k));
						}
					}

					EnerCons_rdfID_List.add(energyConsu_rdfID_temp);

					SQLdatabase.EnergyConsumerTable(energyConsu_rdfID_temp, energyConsu_name, energyConsu_P,
							energyConsu_Q, energyConsu_equipmentContainer_rdfID, energyConsu_baseVoltage_rdfID);

					// System.out.println("RegulatingControl : rdfID: " + rdfID_tempSSH);
					System.out.println("Energy Consumer : rdfID: " + energyConsu_rdfID_temp);
					System.out.println("          name: " + energyConsu_name);
					System.out.println("          P: " + energyConsu_P);
					System.out.println("          Q: " + energyConsu_Q);
					System.out
							.println("          equipmentContainer rdfID " + energyConsu_equipmentContainer_rdfID);
					System.out.println("          base voltage rdfID " + energyConsu_baseVoltage_rdfID);
				}
			}

		}

		// -------------------------------------//
		// ---Load Power Transformer End info---//
		// ------------rdfID, name, P, Q--------//
		// ------------Transformer_rdf:ID-------//
		// ------------baseVoltage_ rdf:ID------//
		// ------Insert values to Power Transformer End Table-----//
		// -------------------------------------//
		List<String> PowetTransEnd_rdfID_List = new ArrayList<String>();
		List<String> PowetTransEnd_terminal_rdfID_List = new ArrayList<String>();

		PowetTransEnd_rxvgb = new double[powerTranEndList.getLength()][5];

		PowerTransformerEnd powerTranEnd = new PowerTransformerEnd();
		for (int i = 0; i < powerTranEndList.getLength(); i++) {
			String powerTranEnd_rdfID = powerTranEnd.rdfID(powerTranEndList.item(i));
			String powerTranEnd_name = powerTranEnd.name(powerTranEndList.item(i));
			double powerTranEnd_r = powerTranEnd.r(powerTranEndList.item(i));
			double powerTranEnd_x = powerTranEnd.x(powerTranEndList.item(i));
			double powerTranEnd_g = powerTranEnd.g(powerTranEndList.item(i));
			double powerTranEnd_b = powerTranEnd.b(powerTranEndList.item(i));
			double powerTranEnd_ratedU = powerTranEnd.ratedU(powerTranEndList.item(i));
			
			String powerTranEnd_Transformer_rdfID = powerTranEnd.Transformer_rdfID(powerTranEndList.item(i));
			String powerTranEnd_baseVoltage_rdfID = powerTranEnd.baseVoltage_rdfID(powerTranEndList.item(i));
			String terminal_rdfID = powerTranEnd.terminal_rdfID(powerTranEndList.item(i));

			PowetTransEnd_rdfID_List.add(powerTranEnd_rdfID);
			PowetTransEnd_terminal_rdfID_List.add(terminal_rdfID);
			PowetTransEnd_rxvgb[i][0] = powerTranEnd_r;
			PowetTransEnd_rxvgb[i][1] = powerTranEnd_x;
			
			for (int j = 0; j < baseVoltList.getLength(); j++) {
				if (powerTranEnd_baseVoltage_rdfID.equals(basevoltage_rdfID_List.get(j))) {
					PowetTransEnd_rxvgb[i][2] = basevoltage_List[j];
				}
			}
			
			PowetTransEnd_rxvgb[i][3] = powerTranEnd_g;
			PowetTransEnd_rxvgb[i][4] = powerTranEnd_b;
			//PowetTransEnd_rxvgb[i][2] = powerTranEnd_ratedU;
			
			SQLdatabase.PowerTransformerEndTable(powerTranEnd_rdfID, powerTranEnd_name, powerTranEnd_r,
					powerTranEnd_x, powerTranEnd_Transformer_rdfID, powerTranEnd_baseVoltage_rdfID);

			System.out.println("power transformer end: rdfID: " + powerTranEnd_rdfID);
			System.out.println("                       name: " + powerTranEnd_name);
			System.out.println("                       r: " + powerTranEnd_r);
			System.out.println("                       x: " + powerTranEnd_x);
			System.out.println("                       Transformer rdfID: " + powerTranEnd_Transformer_rdfID);
			System.out.println("                       base voltage rdfID: " + powerTranEnd_baseVoltage_rdfID);
		}

		// -------------------------------------//
		// ------------Load Breaker info--------//
		// ------------rdfID, name, state-------//
		// equipmentContainer_rdf:ID, baseVoltage_ rdf:ID//
		// ------create table: RatioTapChan-----//
		// -------------------------------------//
		List<String> Breaker_rdfID_List = new ArrayList<String>();
		Breaker Breaker = new Breaker();
		String breaker_rdfID_temp, breaker_rdfID_tempSSH, breaker_name, breaker_equipmentContainer_rdfID,
				breaker_baseVoltagerdfID = "Null";
		boolean breaker_state;
		for (int i = 0; i < breakerList.getLength(); i++) {
			breaker_rdfID_temp = reglCtrl.rdfIDEQ(breakerList.item(i));
			for (int j = 0; j < breakerListSSH.getLength(); j++) {
				breaker_rdfID_tempSSH = Breaker.rdfIDSSH(breakerListSSH.item(j));
				if (breaker_rdfID_temp.equals(breaker_rdfID_tempSSH)) {
					breaker_name = Breaker.name(breakerList.item(i));
					breaker_state = Breaker.state(breakerListSSH.item(j));
					breaker_equipmentContainer_rdfID = Breaker.equipmentContainer_rdfID(breakerList.item(i));

					// TO DO: breaker base voltage
					VoltageLevel volt = new VoltageLevel();
					for (int k = 0; k < voltageLevelList.getLength(); k++) {
						String volytageLevel_rdfID = volt.rdfID(voltageLevelList.item(k));
						if (breaker_equipmentContainer_rdfID.equals(volytageLevel_rdfID)) {
							breaker_baseVoltagerdfID = volt.baseVoltage_rdfID(voltageLevelList.item(k));
						}
					}

					Breaker_rdfID_List.add(breaker_rdfID_temp);

					SQLdatabase.BreakerTable(breaker_rdfID_temp, breaker_name, breaker_state,
							breaker_equipmentContainer_rdfID);
					// System.out.println("RegulatingControl : rdfID: " + rdfID_tempSSH);

					System.out.println("Breaker : rdfID: " + breaker_rdfID_temp);
					System.out.println("          name: " + breaker_name);
					System.out.println("          state: " + breaker_state);
					System.out.println("          equipmentContainer rdfID " + breaker_equipmentContainer_rdfID);
					System.out.println("          base voltage rdfID" + breaker_baseVoltagerdfID);
				}
			}

		}

		// -------------------------------------//
		// -----Load Ratio Tap Changer info-----//
		// ------------rdfID, name, step--------//
		// ------create table: RatioTapChan-----//
		// -------------------------------------//
		RatioTapChanger ratioTapChanger = new RatioTapChanger();
		double ratioTapChang_step = 0.0;
		for (int i = 0; i < ratioTapChangerList.getLength(); i++) {
			String ratioTapChang_rdfID = ratioTapChanger.rdfID(ratioTapChangerList.item(i));
			String ratioTapChang_name = ratioTapChanger.name(ratioTapChangerList.item(i));

			for (int j = 0; j < ratioTapChangerListSSH.getLength(); j++) {
				String ratioTapChang_rdfIDSSH = ratioTapChanger.rdfIDSSH(ratioTapChangerListSSH.item(j));
				if (ratioTapChang_rdfID.equals(ratioTapChang_rdfIDSSH)) {
					ratioTapChang_step = ratioTapChanger.step(ratioTapChangerListSSH.item(j));
				}
			}
			SQLdatabase.RatioTapChangerTable(ratioTapChang_rdfID, ratioTapChang_name, ratioTapChang_step);
			System.out.println("ratio Tap Changer: rdfID: " + ratioTapChang_rdfID);
			System.out.println("                   name: " + ratioTapChang_name);
			System.out.println("                   step: " + ratioTapChang_step);
		}

		// -------------------------------------//
		// -----------Connectivity Node---------//
		// ---------------rdfID-----------------//
		// -------------------------------------//
		List<String> ConnNode_rdfID_List = new ArrayList<String>();
		List<String> ConnNode_cont_rdfID_List = new ArrayList<String>();
		ConnNode CN = new ConnNode();
		for (int i = 0; i < ConnNodeList.getLength(); i++) {
			String ConnNoderdfID = CN.rdfID(ConnNodeList.item(i));
			String ConnNode_contain_rdfID = CN.conatiner_rdfID(ConnNodeList.item(i));
			ConnNode_rdfID_List.add(ConnNoderdfID);
			ConnNode_cont_rdfID_List.add(ConnNode_contain_rdfID);
		}
		//System.out.println(ConnNode_rdfID_List);

		// -------------------------------------//
		// ---------------Terminal--------------//
		// ---------------rdfID-----------------//
		// -------------------------------------//
		List<String> Term_rdfID_List = new ArrayList<String>();
		//List<String> this.Term_CondEqui_List = new ArrayList<String>();
		List<String> Term_ConnNode_List = new ArrayList<String>();
		// ArrayList <BaseVoltClass> BaseVoltageList = new ArrayList<BaseVoltClass>();
		Term term = new Term();
		for (int i = 0; i < TermList.getLength(); i++) {
			String TermrdfID = term.rdfID( TermList.item(i));
			String TermCondEqui_rdfID = term.termEqui_rdfID( TermList.item(i));
			String TermConnNode_rdfID = term.termNode_rdfID( TermList.item(i));

			Term_rdfID_List.add(TermrdfID);
			Term_CondEqui_List.add(TermCondEqui_rdfID);
			Term_ConnNode_List.add(TermConnNode_rdfID);
		}
		/*
		System.out.println(Term_rdfID_List);
		System.out.println(this.Term_CondEqui_List);
		System.out.println(Term_ConnNode_List);
		*/
		
		// -------------------------------------//
		// -------------ACLineSegment-----------//
		// ---------------rdfID-----------------//
		// -------------------------------------//
		List<String> ACLineSegment_rdfID_List = new ArrayList<String>();
		ACLine acLine = new ACLine();
		double[] impedance_length = new double[5];
		rxbglv = new double[ACLineList.getLength()][6];

		for (int i = 0; i < ACLineList.getLength(); i++) {
			String ACLine_rdfID = acLine.rdfID(ACLineList.item(i));
			String ACLine_basevoltage_rdfID = acLine.basevoltage_rdfID(ACLineList.item(i));
			impedance_length = acLine.rxgbl(ACLineList.item(i));
			rxbglv[i][0] = impedance_length[0];
			rxbglv[i][1] = impedance_length[1];
			rxbglv[i][2] = impedance_length[2];
			rxbglv[i][3] = impedance_length[3];
			rxbglv[i][4] = impedance_length[4];
			ACLineSegment_rdfID_List.add(ACLine_rdfID);
			for (int j = 0; j < baseVoltList.getLength(); j++) {
				if (ACLine_basevoltage_rdfID.equals(basevoltage_rdfID_List.get(j))) {
					rxbglv[i][5] = basevoltage_List[j];
				}
			}
		}

		// -------------------------------------//
		// -------------LinearShuntCapa---------//
		// ---------------rdfID-----------------//
		// -------------------------------------//
		List<String> LinearShuntCom_rdfID_List = new ArrayList<String>();
		LinearShuntCom_bgUs = new double[ACLineList.getLength()][4];
		double sectionnumber = 0.0;
		LinearShuntCapa linearShuntCapaList = new LinearShuntCapa();

		for (int i = 0; i < LinearShuntCapaList.getLength(); i++) {
			String linearShunt_rdfID = linearShuntCapaList.rdfID(LinearShuntCapaList.item(i));
			double[] linearShunt = linearShuntCapaList.bgU(LinearShuntCapaList.item(i));
			LinearShuntCom_rdfID_List.add(linearShunt_rdfID);
			LinearShuntCom_bgUs[i][0] = linearShunt[0];
			LinearShuntCom_bgUs[i][1] = linearShunt[1];
			LinearShuntCom_bgUs[i][2] = linearShunt[2];
			for (int j = 0; j < LinearShuntCapaList.getLength(); j++) {
				String linearShunt_rdfIDSSH = linearShuntCapaList.rdfIDSSH(LinearShuntCapaListSSH.item(j));
				if (linearShunt_rdfID.equals(linearShunt_rdfIDSSH)) {
					System.out.println("equal for eq and ssh");
					sectionnumber = linearShuntCapaList.sectionnumber(LinearShuntCapaListSSH.item(j));
					LinearShuntCom_bgUs[i][3] = sectionnumber;
				}
			}
			System.out.println("linear shunt capacitor: rdfID: " + linearShunt_rdfID);
			System.out.println("                        bgUs: " + linearShunt[0] + ", " + linearShunt[1] + ", "
					+ linearShunt[2] + ", " + sectionnumber);

		}

		// -------------------------------------//
		// -------------BusbarSection-----------//
		// ---------------rdfID-----------------//
		// -------------------------------------//
		List<String> busbarSection_rdfID_List = new ArrayList<String>();
		List<String> busbarSection_contain_rdfID_List = new ArrayList<String>();

		BusbarSection busbarSection = new BusbarSection();
		for (int i = 0; i < BusbarSectList.getLength(); i++) {
			String busbarSection_rdfID = busbarSection.rdfID(BusbarSectList.item(i));
			String busbarSection_cont_rdfID = busbarSection.conatiner_rdfID(BusbarSectList.item(i));

			busbarSection_rdfID_List.add(busbarSection_rdfID);
			busbarSection_contain_rdfID_List.add(busbarSection_cont_rdfID);
		}
		/*
		System.out.println(ACLineSegment_rdfID_List);
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println(ConnNode_cont_rdfID_List);
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

		System.out.println(busbarSection_rdfID_List);
		System.out.println(EnerCons_rdfID_List);
		System.out.println(Breaker_rdfID_List);

		System.out.println(ConnNodeList.getLength());
		System.out.println(this.TermList.getLength());
		*/
		
		// ---connect Connectivity Node With busbarsection------//
		int n = 0;
		Connindex = new int[ConnNodeList.getLength()][2];
		//System.out.println((BusbarSectList.getLength() + 1));
		for (int i = 0; i < ConnNodeList.getLength(); i++) {
			Connindex[i][1] = BusbarSectList.getLength() + 1;
			for (int j = 0; j < BusbarSectList.getLength(); j++) {
				if (ConnNode_cont_rdfID_List.get(i).equals(busbarSection_contain_rdfID_List.get(j))) {
					n++;
					// System.out.println(n + "connection: " + "No." + (i + 1) + " ConnectivityNode
					// is connected with "
					// + "No." + (j + 1) + " Terminal");
					Connindex[i][0] = i; // save terminal number
					Connindex[i][1] = j; // save connectivity node number
				}
			}
		}
		
		// ---connect Connectivity Node With Terminal------//
		n = 0;
		Termindex = new int[TermList.getLength()][8];
		for (int i = 0; i < ConnNodeList.getLength(); i++) {

			for (int j = 0; j < TermList.getLength(); j++) {
				if (ConnNode_rdfID_List.get(i).equals(Term_ConnNode_List.get(j))) {
					n++;
					//System.out.println(n + "connection: " + "No." + (i) + " ConnectivityNode is connected with "
					//		+ "No." + (j) + " Terminal");
					Termindex[j][0] = j; // save terminal number
					Termindex[j][1] = i; // save connectivity node number
				}
			}
		}
		
		
		// ---Terminal classification------//
		// Possible conducting equipment: ACLineSegement,breaker, Power Transformer
		// -------------------Synchronous machine, busbarsection, shunt capacitor,
		// energy consumer//
		n = 0;
		// List<int> Current_Te_List = new ArrayList<int>();
		List<String> Term_condEquip_List = new ArrayList<String>();
		for (int i = 0; i <  TermList.getLength(); i++) {
			String term_rdfID_temp = Term_CondEqui_List.get(i);
			String equip_rdfID_temp;

			// ------go through Busbarsection list--------//
			for (int j = 0; j < BusbarSectList.getLength(); j++) {
				equip_rdfID_temp = busbarSection_rdfID_List.get(j);
				if (term_rdfID_temp.equals(equip_rdfID_temp)) {
					Termindex[i][2] = 1; // 1 for busbarsection
					Termindex[i][4] = j;
				}
			}

			// ------go through Sync_rdfID_List list--------//
			for (int j = 0; j < syncMachList.getLength(); j++) {
				equip_rdfID_temp = Sync_rdfID_List.get(j);
				if (term_rdfID_temp.equals(equip_rdfID_temp)) {
					Termindex[i][2] = 2; // 2 for syncMach
					Termindex[i][4] = j;
				}
			}

			// ------go through ACLine list--------//
			for (int j = 0; j < ACLineList.getLength(); j++) {
				equip_rdfID_temp = ACLineSegment_rdfID_List.get(j);
				if (term_rdfID_temp.equals(equip_rdfID_temp)) {
					Termindex[i][2] = 3; // 3 for AC Line Segment
					Termindex[i][4] = j;
				}
			}

			// ------go through PowerTran_rdfID list--------//
			for (int j = 0; j < powerTranList.getLength(); j++) {
				equip_rdfID_temp = PowerTran_rdfID_List.get(j);
				if (term_rdfID_temp.equals(equip_rdfID_temp)) {
					Termindex[i][2] = 4; // 4 for PowerTran
					Termindex[i][4] = j;
					for (int k = 0; k < powerTranEndList.getLength(); k++) {
						if (PowetTransEnd_terminal_rdfID_List.get(k).equals(Term_rdfID_List.get(i))) {
							Termindex[i][7] = k;
						}
					}
				}
			}
			
			
			

			// ------go through Breaker list--------//
			for (int j = 0; j < breakerList.getLength(); j++) {
				equip_rdfID_temp = Breaker_rdfID_List.get(j);
				if (term_rdfID_temp.equals(equip_rdfID_temp)) {
					Termindex[i][2] = 5; // 5 for Breaker
					Termindex[i][4] = j;
				}
			}

			// ------go through LinearShuntCapaList list--------//
			for (int j = 0; j < LinearShuntCapaList.getLength(); j++) {
				equip_rdfID_temp = LinearShuntCom_rdfID_List.get(j);
				if (term_rdfID_temp.equals(equip_rdfID_temp)) {
					Termindex[i][2] = 6; // 6 for LinearShuntCapaList
					Termindex[i][4] = j;
				}
			}

			// ------go through energyConsumer List--------//
			for (int j = 0; j < energyConsumerList.getLength(); j++) {
				equip_rdfID_temp = EnerCons_rdfID_List.get(j);
				if (term_rdfID_temp.equals(equip_rdfID_temp)) {
					Termindex[i][2] = 7; // 7 for energy consumer
					Termindex[i][4] = j;
				}
			}
		}
	 
	 
 }
 
 public int[][] Termindex(){
	 return Termindex;
 }
 
 public int[][] Connindex(){
	 return Connindex;
 }
 
 public List<String> Term_CondEqui_List(){
	 return Term_CondEqui_List;
 }
 
 public NodeList TermList(){
	 return TermList;
 }
 
 public NodeList ConnNodeList(){
	 return ConnNodeList;
 }
 
 public NodeList BusbarSectList(){
	 return BusbarSectList;
 }
 
 public double syncMach_ratedS(){
	 return syncMach_ratedS;
 }
 
 public double[][] LinearShuntCom_bgUs(){
	 return LinearShuntCom_bgUs;
 }

 public double[][] rxbglv(){
	 return rxbglv;
 }

 public double[][] PowetTransEnd_rxvgb(){
	 return PowetTransEnd_rxvgb;
 }

}
