Group#02
We have created our main method inside LoadCIM class.

Inside the main method, firstly, we have called our SQL database class that cotains StartUp method, which is basically creating the connnection between eclipse and
MySQL. After that we are calling the method createTables inside the same class to create the required tables in the database.  

And then we parsed two files into mainParsing method inside ParseCIM class. Then we have created the NodeList that is extracting all the values and inserting into the List.
Inside the for loop we are extracting the values (using the different classes according to the CIM Objects) and inserting the values into the created tables. For example, for baseVoltage we have created
a class called baseVoltage which contains the methods based on the required data. We are extracting these data in the for loop from that baseVoltage class. Finally,
we called the respective method from SQLdatabase class to insert the values into the table.

