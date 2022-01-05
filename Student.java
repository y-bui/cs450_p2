import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import oracle.jdbc.driver.*;
import org.apache.ibatis.jdbc.ScriptRunner;



public class Student{
    static Connection con;
    static Statement stmt;
    static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args)
    {
		connectToDatabase();
		runOrScript();
		int count = 1;
		while (count==1) {
			printMenu();
			System.out.println("Select a menu option (1-4)");
			int inp = sc.nextInt();
			sc.nextLine();
			if (inp<1 || inp>4) System.out.println("Choice not included in menu. Please try again.");
			else if (inp==1) choiceOne();
			else if (inp==2) choiceTwo();
			else if (inp==3) choiceThree();
			else if (inp==4) count=0;
		}
		System.out.println("Thanks for using our program!");
		return;
    }

    public static void choiceOne() {
    	//Scanner sc = new Scanner(System.in);
    	System.out.println("Product(Yes/No)");
    	String inp = sc.nextLine();
    	if (inp == "Yes" || inp == "yes" || inp == "YES") {
    		try{
    			ResultSet rs = stmt.executeQuery("select * from product");
    			while (rs.next()) {
        			System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3)
        			+" "+rs.getString(4)+" "+rs.getString(5)+" "+rs.getFloat(6)
        			+" "+rs.getInt(7));
        		}
    		} catch(SQLException e) { 
    			System.out.println("Unable to print from table Products");
    			e.printStackTrace();
    		}
    		
    	}
    	System.out.println("Customer(Yes/No)");
    	inp = sc.nextLine();
    	if (inp == "Yes" || inp == "yes" || inp == "YES") {
    		try{
    			ResultSet rs = stmt.executeQuery("select * from customer");
    			while (rs.next()) {
        			System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3)
        			+" "+rs.getInt(4)+" "+rs.getString(5).charAt(0)+" "+rs.getInt(6));
        		}
    		} catch(SQLException e) { 
    			System.out.println("Unable to print from table Customer");
    			e.printStackTrace();
    		}
    	}
    	System.out.println("Transactions(Yes/No)");
    	inp = sc.nextLine();
    	if (inp == "Yes" || inp == "yes" || inp == "YES") {
    		try{
    			ResultSet rs = stmt.executeQuery("select * from transactions");
    			while (rs.next()) {
        			System.out.println(rs.getInt(1)+" "+rs.getInt(2)+" "+rs.getDate(3)
        			+" "+rs.getString(4).charAt(0)+" "+rs.getFloat(5));
        		}
    		} catch(SQLException e) { 
    			System.out.println("Unable to print from table Transactions");
    			e.printStackTrace();
    		}
    	}
    	System.out.println("Transaction_Contains (Yes/No)");
    	inp = sc.nextLine();
    	if (inp == "Yes" || inp == "yes" || inp == "YES") {
    		try{
    			ResultSet rs = stmt.executeQuery("select * from transaction_contains");
    			while (rs.next()) {
        			System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getInt(3));
        		}
    		} catch(SQLException e) { 
    			System.out.println("Unable to print from table Transaction_Contains");
    			e.printStackTrace();
    		}
    	}
    }
    
    public static void choiceTwo() {
    	System.out.println("Enter transaction_ID: ");
    	int trID = sc.nextInt();
    	sc.nextLine();
    	String s = "";
		s += "select Transactions.transaction_ID, Transactions.customer_ID, Transactions.transaction_date, Transactions.payment_method, Transactions.total, AVG(Transaction_Contains.quantity)";
		s += " from Transactions, Transaction_Contains";
		s += " where Transactions.transaction_id=Transaction_Contains.transaction_id and Transactions.transaction_id=";
		s += trID;
		s += " group by Transactions.transaction_ID, Transactions.customer_ID, Transactions.transaction_date, Transactions.payment_method, Transactions.total";
    	try {
    		ResultSet rs = stmt.executeQuery(s);
    		while (rs.next()) {
    			System.out.println(rs.getInt(1)+" "+rs.getInt(2)+" "+rs.getDate(3)+" "+rs.getString(4).charAt(0)+" "+rs.getFloat(5));//+" "+rs.getFloat(6));
    		}
    	} catch (SQLException e) {
    		System.out.println("Unable to perform query from ID. Try again.");
    		e.printStackTrace();
    		return;
    	}
    }
    
    public static void choiceThree() {
    	System.out.println("Input Fields");
  
    	System.out.println("customer_ID:");
    	int custID = sc.nextInt();
    	sc.nextLine();
    	System.out.println("total:");
    	float tot = sc.nextFloat();
    	sc.nextLine();
    	System.out.println("UPC:");
    	int upc = sc.nextInt();
    	sc.nextLine();
    	System.out.println("quantity:");
    	int quantity = sc.nextInt();
    	sc.nextLine();
    	
    	System.out.println();
    	System.out.println("Output Fields");
    	char[] cc = new char[7];
    	
    	//Transactions
    	System.out.println("transaction_ID (Yes/No)");
    	cc[0] = sc.next().charAt(0);
    	System.out.println("customer_ID (Yes/No)");
    	cc[1] = sc.next().charAt(0);
    	System.out.println("transaction_date (Yes/No)");
    	cc[2] = sc.next().charAt(0);
    	System.out.println("payment_method (Yes/No)");
    	cc[3] = sc.next().charAt(0);
    	System.out.println("total (Yes/No)");
    	cc[4] = sc.next().charAt(0);
    	//Transaction_Contains
    	System.out.println("UPC (Yes/No)");
    	cc[5] = sc.next().charAt(0);
    	System.out.println("quantity (Yes/No)");
    	cc[6] = sc.next().charAt(0);
    	
    	if (sc.hasNext()) sc.nextLine();
    	
    	//make checker -- if yes, add Transactions.transaction_ID to string s to build query
    	
    	String s = "select ";
    	for (int i=0; i<cc.length; i++) {
    		if (cc[i] == 'Y' || cc[i] == 'y') {
    			switch (i) {
    				case 0: s += "Transactions.transaction_ID, "; break;
    				case 1: s += "Transactions.customer_ID, "; break;
    				case 2: s += "Transactions.transaction_date, "; break;
    				case 3: s += "Transactions.payment_method"; break;
    				case 4: s += "Transactions.total, "; break;
    				case 5: s += "Transaction_Contains.upc, "; break;
    				case 6: s += "Transaction_Contains.quantity, "; break;
    			}
    		}
    	}
    	if (s.length() < 7) {
    		System.out.println("Must select at least one field. Try again.\n");
    		return;
    	}
    	
    	s.trim(); //remove last space or any white space in front or end
    	s = s.substring(0, s.length()-1); //remove last comma
    	s += " from Transactions";
    	if (cc[5] == 'Y' || cc[6] == 'Y' || cc[5] == 'y' || cc[6] == 'y') {
    		s += ", Transaction_Contains where Transactions.transaction_id=Transaction_Contains.transaction_id";
    		s += " and Transactions.customer_id=";
    		s += custID;
    		s += " and Transactions.total=";
    		s += tot;
    		s += " and Transactions_Contains.upc=";
    		s += upc;
    		s += " and Transaction_Contains.quantity=";
    		s += quantity;
    	}
    	else {
    		s += " where Transactions.customer_id=";
    		s += custID;
    		s += " and Transactions.total=";
    		s += tot;
    	}
    	System.out.println(s);
    	return;
    }
    
    public static void runOrScript() {
    	ScriptRunner sr = new ScriptRunner(con);
    	System.out.println("Enter location of script (inclusive of the *.sql file): ");
    	String x = sc.nextLine();
    	try{
    		Reader r = new BufferedReader(new FileReader(x));
    		System.out.println("Executing file ...");
    		sr.runScript(r);
    	} catch (Exception e) {
    		System.out.println("File not found");
    		return;
    	}
    }
    
    public static void printMenu() {
    	System.out.println("1. View table contents");
    	System.out.println("2. Search by Transaction_ID");
    	System.out.println("3. Search by one or more attributes");
    	System.out.println("4. Exit");
    }
    
    public static void connectToDatabase()
    {
	String driverPrefixURL="jdbc:oracle:thin:@";
	String jdbc_url="artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
		System.out.println("Enter username:");
        // IMPORTANT: DO NOT PUT YOUR LOGIN INFORMATION HERE. INSTEAD, PROMPT USER FOR HIS/HER LOGIN/PASSWD
    	String username=sc.nextLine();
    	System.out.println("Enter password:");
        String password=sc.nextLine();
	
        try{
	    //Register Oracle driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (Exception e) {
            System.out.println("Failed to load JDBC/ODBC driver.");
            return;
        }

       try{
            System.out.println(driverPrefixURL+jdbc_url);
            con=DriverManager.getConnection(driverPrefixURL+jdbc_url, username, password);
            DatabaseMetaData dbmd=con.getMetaData();
            stmt=con.createStatement();

            System.out.println("Connected.");

            if(dbmd==null){
                System.out.println("No database meta data");
            }
            else {
                System.out.println("Database Product Name: "+dbmd.getDatabaseProductName());
                System.out.println("Database Product Version: "+dbmd.getDatabaseProductVersion());
                System.out.println("Database Driver Name: "+dbmd.getDriverName());
                System.out.println("Database Driver Version: "+dbmd.getDriverVersion());
            }
        }catch( Exception e) {e.printStackTrace();}

    }// End of connectToDatabase()
}// End of class


