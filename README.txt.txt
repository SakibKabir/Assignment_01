-EH2745 Computer Application in Power System
-Assignment 1
-Group#02
-Sakib 
-Shuyu[shuyuou13@gmail.com]
-2018 May 1st-2018 May 15th

/////////////////////////////////////////////////////////
-Function of codes:
-1. Load microgrid CIM file
-2. Parsing CIM file 
-3. Connected to MySQL, create table and save desired data
-4. Build up Y bus matrix

/////////////////////////////////////////////////////////
-Function need to do or inhanced:
-1. GUI, input username and password, 
    output Y bus matrix and basic information of microgrid

/////////////////////////////////////////////////////////
-Main Structure
-Main method<LoadCIM.java>:
            
-T1: Parsing CIM <ParseCIM.java>
     Connect and traverse different subclass 
-T2: MySQL part <SQLdatabase.java>
     Connection, create table, insert values
-T3: Ybus matrix <Ybus.java>
     Connect different components and calculate y bus matrix
     
/////////////////////////////////////////////////////////
-Analysis of certain class
- <LoadCIM.java>
- 1. Call SQL database -> connection between eclipse and mySQL built up.
- 2. Run ParseCIM with EQ and SSH files, traverse objects<AC line, breaker, busbarsection, connectivity node, energy consumer, generating unit, linear shunt compensator, power transformer, power transformer end, ratio tap changer, regulating control, synchronous machine, voltage level, base voltage, substation>, derive elements or attributes, for instance, rdf ID, object name and basevoltage rdf ID. Save the data into MySQL table.
- 3. Ybus matrix -> connect different components with rdf ID, storage connection relation into an index matrix <Termindex> -> go through busbar and connected objects,if it is a single port device, then move to another device, if it's a two port model, expend the connection until it reach to another busbar, for example, [bus 1----terminal 1]----[terminal 2-----breaker------terminal4]------[terminal5 -------breaker----------terminal 3]---[terminal 7-----bus 4].

