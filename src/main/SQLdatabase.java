package main;

import java.sql.*;

public class SQLdatabase {

	public static Connection conn = null;
	public static Statement stmt = null;
	public static String sql = null;

	// StartUp method for connecting to SQL server and creating the desirable
	// database
	public void StartUp() throws Exception {

			  try{
		    	   String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
	    		   String DB_URL = "jdbc:mysql://localhost/";
	    		   String username = "root";
	    		   String password = "01911570066@Aiub";
	    		   
			      // Register JDBC driver
			      Class.forName(JDBC_DRIVER);
			      
			      // Open a connection
			      System.out.println("Connecting to SQL server...");
			      conn = DriverManager.getConnection(DB_URL, username, password);

			      // Execute a query to create database MicroGrid
			      System.out.println("Creating database...");
			      stmt = conn.createStatement(); 
			      sql = "DROP DATABASE IF EXISTS MicroGrid";
			      stmt.executeUpdate(sql);
			    
			      sql = "CREATE DATABASE MicroGrid"; 
			      stmt.executeUpdate(sql);
			      System.out.println("Database created successfully..."); 
			      
			      // Connect to the created database MicroGrid
			       conn = DriverManager.getConnection(DB_URL + "MicroGrid", username, password);
			       sql = "USE MicroGrid"; 
			       stmt.executeUpdate(sql) ; 
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}
	}
	
	public void createTables() throws Exception{
		
		try{
		
			sql = "CREATE TABLE IF NOT EXISTS BaseVoltage"
					+ "(rdfID VARCHAR(40) NOT NULL, NominalValue DOUBLE, PRIMARY KEY (rdfID))";
			stmt.executeUpdate(sql) ;
			System.out.println("Created BaseVoltage table in MicroGrid database successfully...");
			
		
			sql = "CREATE TABLE IF NOT EXISTS Substation"
					+ "(rdfID VARCHAR(45) NOT NULL, Name VARCHAR(45), Region_rdfID VARCHAR(50),"
					+ "PRIMARY KEY (rdfID))";
			stmt.executeUpdate(sql) ;
			System.out.println("Created Substation table in MicroGrid database successfully...");
			
			// Create Voltage Level table with corresponding attributes
			sql = "CREATE TABLE IF NOT EXISTS Voltage_Level"
					+ "(rdfID VARCHAR(40) NOT NULL, Name VARCHAR(40), Substation_rdfID VARCHAR(40),"
					+ "BaseVoltage_rdfID VARCHAR(40), PRIMARY KEY (rdfID),"
					+ "FOREIGN KEY (Substation_rdfID) REFERENCES Substation(rdfID),"
					+ "FOREIGN KEY (BaseVoltage_rdfID) REFERENCES BaseVoltage(rdfID))";
			stmt.executeUpdate(sql) ; 
			System.out.println("Created VoltageLevel table in MicroGrid database successfully...");
			
			// Create Generating Unit table with corresponding attributes
			sql = "CREATE TABLE IF NOT EXISTS Generating_Unit"
					+ "(rdfID VARCHAR(40) NOT NULL, Name VARCHAR(40), MaxP DOUBLE, MinP DOUBLE,"
					+ "EquipmentContainer_rdfID VARCHAR(40), PRIMARY KEY (rdfID),"
					+ "FOREIGN KEY (EquipmentContainer_rdfID) REFERENCES Substation(rdfID))";
			stmt.executeUpdate(sql) ; // execute query
			System.out.println("Created GeneratingUnit table in MicroGrid database successfully...");
						
			// Create Synchronous Machine table with corresponding attributes
			sql = "CREATE TABLE IF NOT EXISTS Synchronous_Machine"
					+ "(rdfID VARCHAR(40) NOT NULL, Name VARCHAR(40), RatedS DOUBLE, P DOUBLE, Q DOUBLE,"
					+ "GenUnit_rdfID VARCHAR(40), RegControl_rdfID VARCHAR(40),"
					+ "EquipmentContainer_rdfID VARCHAR(40), baseVoltage_rdfID VARCHAR(40), PRIMARY KEY (rdfID))";
					//+ "FOREIGN KEY (GenUnit_rdfID) REFERENCES GeneratingUnit(rdfID),"
					//+ "FOREIGN KEY (RegControl_rdfID) REFERENCES RegulatingControl(rdfID),"
					//+ "FOREIGN KEY (EquipmentContainer_rdfID) REFERENCES VoltageLevel(rdfID))";
			stmt.executeUpdate(sql) ; // execute query
			System.out.println("Created SynchronousMachine table in MicroGrid database successfully...");
			
			// Create Regulating Control table with corresponding attributes
			sql = "CREATE TABLE IF NOT EXISTS Regulating_Control"
					+ "(rdfID VARCHAR(40) NOT NULL, Name VARCHAR(40), TargetValue DOUBLE,"
					+ "PRIMARY KEY (rdfID))";
			stmt.executeUpdate(sql) ; // execute query
			System.out.println("Created RegulatingControl table in MicroGrid database successfully...");
						
			// Create Power Transformer table with corresponding attributes
			sql = "CREATE TABLE IF NOT EXISTS Power_Transformer"
					+ "(rdfID VARCHAR(40) NOT NULL, Name VARCHAR(40),"
					+ "EquipmentContainer_rdfID VARCHAR(40), PRIMARY KEY (rdfID),"
					+ "FOREIGN KEY (EquipmentContainer_rdfID) REFERENCES Substation(rdfID))";
			stmt.executeUpdate(sql) ; // execute query
			System.out.println("Created PowerTransformer table in MicroGrid database successfully...");
			
			// Create Energy Consumer table with corresponding attributes
			sql = "CREATE TABLE IF NOT EXISTS Energy_Consumer"
					+ "(rdfID VARCHAR(40) NOT NULL, Name VARCHAR(40), P DOUBLE, Q DOUBLE,"
					+ "EquipmentContainer_rdfID VARCHAR(40), baseVoltage_rdfID VARCHAR(40), PRIMARY KEY (rdfID),"
					+ "FOREIGN KEY (EquipmentContainer_rdfID) REFERENCES Voltage_Level(rdfID))";
			stmt.executeUpdate(sql);
			System.out.println("Created EnergyConsumer table in MicroGrid database successfully...");
			// Create Transformer Winding table with corresponding attributes
			sql = "CREATE TABLE IF NOT EXISTS Power_Transformer_End"
					+ "(rdfID VARCHAR(40) NOT NULL, Name VARCHAR(40), TransformerR DOUBLE,"
					+ "TransformerX DOUBLE, Transformer_rdfID VARCHAR(40), BaseVoltage_rdfID VARCHAR(40), PRIMARY KEY (rdfID), "
					+ "FOREIGN KEY(Transformer_rdfID) REFERENCES Power_Transformer(rdfID),"
					+ "FOREIGN KEY(BaseVoltage_rdfID) REFERENCES BaseVoltage(rdfID))";
			stmt.executeUpdate(sql) ; // execute query
			System.out.println("Created TransformerWinding table in MicroGrid database successfully...");
			
			// Create Breaker table with corresponding attributes
			sql = "CREATE TABLE IF NOT EXISTS Breaker"
					+ "(rdfID VARCHAR(40) NOT NULL, Name VARCHAR(40), State BOOLEAN,"
					+ "EquipmentContainer_rdfID VARCHAR(40), PRIMARY KEY (rdfID),"
					+ "FOREIGN KEY (EquipmentContainer_rdfID) REFERENCES Voltage_Level(rdfID))";
			stmt.executeUpdate(sql) ; // execute query
			System.out.println("Created Breaker table in MicroGrid database successfully...");
			
			// Create Ratio Tap Changer table with corresponding attributes
			sql = "CREATE TABLE IF NOT EXISTS Ratio_Tap_Changer"
					+ "(rdfID VARCHAR(40) NOT NULL, Name VARCHAR(40), Step DOUBLE,"
					+ "PRIMARY KEY (rdfID))";					
			stmt.executeUpdate(sql) ; // execute query
			System.out.println("Created RatioTapChanger table in MicroGrid database successfully...");
		   }
		
		catch(SQLException se){
		    //Handle errors for JDBC
		    se.printStackTrace();
		    }
		catch(Exception e){
		    //Handle errors for Class.forName
		    e.printStackTrace();
		    }
		}
	//------Methods for inserting Values in the table-----//
	//------For Base Voltage Table-----//
	public static void BaseVoltageTable(String baseVolt_rdfID, double baseVolt_nominalVoltage) {
		try {
			// Create the java MySQL update PreparedStatement 
			String query = "INSERT INTO BaseVoltage VALUES(?,?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, baseVolt_rdfID);
			preparedStmt.setDouble(2, baseVolt_nominalVoltage);
			preparedStmt.executeUpdate(); // execute PreparedStatement
			System.out.println("Inserted Values into BaseVoltage table successfully...");
		   }
		catch(SQLException se){
	    //Handle errors for JDBC
		se.printStackTrace();}
		catch(Exception e){
		//Handle errors for Class.forName
		e.printStackTrace();}	
	}
	//------For Substation Table-----//
	public static void SubstationTable(String rdfID, String Name, String Region_rdfID){
		try {
			String query = "INSERT INTO Substation VALUES(?,?,?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1,rdfID);
			preparedStmt.setString(2,Name);
			preparedStmt.setString(3,Region_rdfID);
			preparedStmt.executeUpdate();
			System.out.println("Inserted Values into Substation table successfully...");
		   }
		catch(SQLException se){
		se.printStackTrace();}
		catch(Exception e){
		e.printStackTrace();}			
		}
	  //------For Voltage Level Table-----//
	 public static void VoltageLevelTable(String rdfID, String Name, String Substation_rdfID, String BaseVoltage_rdfID){
		try {
			String query = "INSERT INTO Voltage_Level VALUES(?,?,?,?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1,rdfID);
			preparedStmt.setString(2,Name);
			preparedStmt.setString(3,Substation_rdfID);
			preparedStmt.setString(4,BaseVoltage_rdfID);
			preparedStmt.executeUpdate();
			System.out.println("Inserted Values into Voltage Level table successfully...");
		   }
		catch(SQLException se){
		se.printStackTrace();}
		catch(Exception e){
		e.printStackTrace();}			
		}
	 
	 //------For Generating Unit Table-----//
	 public static void GeneratingUnitTable(String geneUnit_rdfID, String geneUnit_name, double geneUnit_maxP,
				double geneUnit_minP, String geneUnit_equipCont_rdfID) {
			// TODO Auto-generated method stub
		 try {
				String query = "INSERT INTO Generating_Unit VALUES(?,?,?,?,?)";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1,geneUnit_rdfID);
				preparedStmt.setString(2,geneUnit_name);
				preparedStmt.setDouble(3,geneUnit_maxP);
				preparedStmt.setDouble(4,geneUnit_minP);
				preparedStmt.setString(5,geneUnit_equipCont_rdfID);
				preparedStmt.executeUpdate();
				System.out.println("Inserted Values into Generating Unit table successfully...");
			   }
			catch(SQLException se){
			se.printStackTrace();}
			catch(Exception e){
			e.printStackTrace();}			
	 }
	
	//------For Synchronous Machine Table-----//
	public static void SynchronousMachineTable(String rdfID, String Name, double RatedS, double P, double Q,
			String GenUnit_rdfID, String RegControl_rdfID, String EquipmentContainer_rdfID, String syncMach_baseVoltage_rdfID){
		try {
		String query = "INSERT INTO Synchronous_Machine VALUES(?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1,rdfID);
			preparedStmt.setString(2,Name);
			preparedStmt.setDouble(3,RatedS);
			preparedStmt.setDouble(4,P);
			preparedStmt.setDouble(5,Q);
			preparedStmt.setString(6,GenUnit_rdfID);
			preparedStmt.setString(7,RegControl_rdfID);
			preparedStmt.setString(8,EquipmentContainer_rdfID);
			preparedStmt.setString(9,syncMach_baseVoltage_rdfID);
			preparedStmt.executeUpdate();
			System.out.println("Inserted Values into Synchronous Machine table successfully...");
		   }
		catch(SQLException se){
		se.printStackTrace();}
		catch(Exception e){
		e.printStackTrace();}			
		}
	
	public static void RegulatingControlTable(String rdfID, String Name, double TargetValue){
		try {
			String query = "INSERT INTO Regulating_Control VALUES(?,?,?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1,rdfID);
			preparedStmt.setString(2,Name);
			preparedStmt.setDouble(3,TargetValue);
			preparedStmt.executeUpdate();
			System.out.println("Inserted Values into Regualting Control table successfully...");
		   }
		catch(SQLException se){
		se.printStackTrace();}
		catch(Exception e){
		e.printStackTrace();}			
		}
	
	public static void PowerTransformerTable(String rdfID, String Name, String EquipmentContainer_rdfID){
		try {
			String query = "INSERT INTO Power_Transformer VALUES(?,?,?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1,rdfID);
			preparedStmt.setString(2,Name);
			preparedStmt.setString(3,EquipmentContainer_rdfID);
			preparedStmt.executeUpdate();
			System.out.println("Inserted Values into Power Transformer table successfully...");
		   }
		catch(SQLException se){
		se.printStackTrace();}
		catch(Exception e){
		e.printStackTrace();}			
		}
	    public static void EnergyConsumerTable(String energyConsu_rdfID_temp, String energyConsu_name,
			double energyConsu_P, double energyConsu_Q, String energyConsu_equipmentContainer_rdfID,
			String baseVoltage_rdfID) {
		// TODO Auto-generated method stub
		try {
			String query = "INSERT INTO Energy_Consumer VALUES(?,?,?,?,?,?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1,energyConsu_rdfID_temp);
			preparedStmt.setString(2,energyConsu_name);
			preparedStmt.setDouble(3,energyConsu_P);
			preparedStmt.setDouble(4,energyConsu_Q);
			preparedStmt.setString(5,energyConsu_equipmentContainer_rdfID);
			preparedStmt.setString(6,baseVoltage_rdfID);
			preparedStmt.executeUpdate();
			System.out.println("Inserted Values into Energy Consumer table successfully...");
		   }
		catch(SQLException se){
		se.printStackTrace();}
		catch(Exception e){
		e.printStackTrace();}
					
		}
	
	public static void PowerTransformerEndTable(String rdfID, String Name, double TransformerR, double TransformerX,
			String Transformer_rdfID, String BaseVoltage_rdfID){
		try {
			String query = "INSERT INTO Power_Transformer_End VALUES(?,?,?,?,?,?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1,rdfID);
			preparedStmt.setString(2,Name);
			preparedStmt.setDouble(3,TransformerR);
			preparedStmt.setDouble(4,TransformerX);
			preparedStmt.setString(5,Transformer_rdfID);
			preparedStmt.setString(6,BaseVoltage_rdfID);
			preparedStmt.executeUpdate();
			System.out.println("Inserted Values into Power Transformer End table successfully...");
		   }
		catch(SQLException se){
		se.printStackTrace();}
		catch(Exception e){
		e.printStackTrace();}			
		}
	
	public static void BreakerTable(String rdfID, String Name, boolean State, String EquipmentContainer_rdfID){
		try {
			String query = "INSERT INTO Breaker VALUES(?,?,?,?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1,rdfID);
			preparedStmt.setString(2,Name);
			preparedStmt.setBoolean(3,State);
			preparedStmt.setString(4,EquipmentContainer_rdfID);
			preparedStmt.executeUpdate();
			System.out.println("Inserted Values into Breaker table successfully...");
		   }
		catch(SQLException se){
		se.printStackTrace();}
		catch(Exception e){
		e.printStackTrace();}			
		}	
	

	public static void RatioTapChangerTable(String ratioTapChang_rdfID, String ratioTapChang_name,
			double ratioTapChang_step) {
		// TODO Auto-generated method stub
		try {
			String query = "INSERT INTO Ratio_Tap_Changer VALUES(?,?,?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1,ratioTapChang_rdfID);
			preparedStmt.setString(2,ratioTapChang_name);
			preparedStmt.setDouble(3,ratioTapChang_step);
			preparedStmt.executeUpdate();
			System.out.println("Inserted Values into Ratio_Tap_Changer table successfully...");
		   }
		catch(SQLException se){
		se.printStackTrace();}
		catch(Exception e){
		e.printStackTrace();}			
		}
}


