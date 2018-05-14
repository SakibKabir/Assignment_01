package main;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class LoadCIM {

	public static void main(String[] args) throws Exception {

		try {
			
			//SQL database connection build up //
			SQLdatabase database = new SQLdatabase();
			database.StartUp();
			database.createTables();
			
			// parse CIM file and insert values into SQL table //
			ParseCIM cim = new ParseCIM();
			File EQ = new File("Assignment_EQ_reduced.xml");
			File SSH = new File("Assignment_SSH_reduced.xml");
			cim.mainParsing(EQ, SSH);
			
			// save the list required by YBus //
			int[][] Termindex;
			int[][] Connindex;
			List<String> Term_CondEqui_List;
			NodeList TermList;
			NodeList ConnNodeList;
			NodeList BusbarSectList;
			double syncMach_ratedS;
			double[][] LinearShuntCom_bgUs;
			double[][] rxbglv;
			double[][] PowetTransEnd_rxvgb;
			Termindex = cim.Termindex();
			Connindex = cim.Connindex();
			Term_CondEqui_List = cim.Term_CondEqui_List();
			TermList = cim.TermList();//docEQ.getElementsByTagName("cim:Terminal");
			ConnNodeList = cim.ConnNodeList();
			BusbarSectList = cim.BusbarSectList();
			syncMach_ratedS = cim.syncMach_ratedS();
			LinearShuntCom_bgUs = cim.LinearShuntCom_bgUs();
			rxbglv = cim.rxbglv();
			PowetTransEnd_rxvgb = cim.PowetTransEnd_rxvgb();
			
			// calculate Y bus matrix //
			Ybus callYbus = new Ybus();
			callYbus.ybus(Termindex, Connindex, Term_CondEqui_List, TermList, ConnNodeList, BusbarSectList, syncMach_ratedS, LinearShuntCom_bgUs, rxbglv, PowetTransEnd_rxvgb );
	
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	
}