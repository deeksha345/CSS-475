package css475java;

import java.sql.*;
import java.io.Console;
import java.io.File;
import org.json.JSONObject;
import org.json.JSONException;

/*
 * TEAM 04 
 * TEAM MEMBERS: DYLAN DESMOND, DEEKSHA SHARMA 
 * 
 * This is the main class for interacting with the Access database. Each public method on this class is meant
 * to perform a database operation except for the special function "loaded" that should not be changed or
 * removed. This function is used in the JavaScript code on each web page to deal with timing of the overall
 * Java application interface being ready for further calls.
 * 
 */
public class JSInterface {
	//WHERE SHOULD THE DATABASE BE LOCATED IN THE PACKAGE EXPLORER 
	String connectionString = "jdbc:ucanaccess://./VaccineDatabase.accdb";

	 public Boolean loaded() {
		 return true;
	 }
	 
	 public String getPatientList() {
		 
		 /* Perform database operations */
		 try {
			 
			 /* Attempt to connect to database */
			 Connection conn = DriverManager.getConnection(connectionString);

			 /* Prepare SQL for Execution */
			 String sql = "SELECT patientNumber,firstName,lastName FROM Patient ORDER BY lastName";
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 
			 /* Perform Query Operation */
			 ResultSet rows = stmt.executeQuery();
			 
			 /* Convert Output into a JSON formatted string to return to caller */
			 StringBuilder results = new StringBuilder();
			 results.append("{ \"data\": [");
			 String connector = "";
			 while ( rows.next() ) {
				 results.append(connector);
				 connector = ",";
				 results.append("{");
				 results.append("\"patientNumber\": \"");
				 results.append(rows.getString("patientNumber"));
				 results.append("\",\n\"firstName\": \"");
				 results.append(rows.getString("firstName"));
				 results.append("\",\n\"lastName\": \"");
				 results.append(rows.getString("lastName"));
				 results.append("\"}");
			 }
			 results.append("]}");
			 
			 /* Conclude Connection */
			 conn.close();
			 
			 /* Return Results Back to Caller */
			 return results.toString();
		 }
		 catch (SQLException e) {
			 /* Return Error as JSON Formatted String */
			 return "{ \"error\": \"" + e.getMessage() + "<br>" + connectionString + "\"}";
		 }
 	}

	 //WHY DOES THIS TAKE IN id AS ARGUMENT/ WHAT IS IT USED FOR
	 public String getPatient(String id) {
		 try {
			 
			 /* Attempt to connect to database */
			 Connection conn = DriverManager.getConnection(connectionString);
			 
			 /* Prepare SQL for Execution - Note that each input parameter is marked with a question mark*/
			 String sql = "SELECT patientNumber,firstName,lastName,phone,email,streetAddress,cityName,"
			 		+ "state,country,zipCode FROM Patient P INNER JOIN Address A ON P.addressid = "
			 		+ "A.addressid WHERE patientNumber = ?";
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 
			 /* Provide a value for each input parameter */
			 stmt.setInt(1, Integer.parseInt(id));

			 /* Perform Query Operation */
			 ResultSet rows = stmt.executeQuery();
			 
			 /* Convert Output into a JSON formatted string to return to caller */
			 StringBuilder results = new StringBuilder();
			 results.append("{ \"data\": [");
			 String connector = "";
			 while (rows.next() ) {
				 results.append(connector);
				 connector = ",";
				 results.append("{");
				 results.append("\"patientNumber\": \"");
				 results.append(rows.getString("patientNumber"));
				 results.append("\",\n\"firstName\": \"");
				 results.append(rows.getString("firstName"));
				 results.append("\",\n\"lastName\": \"");
				 results.append(rows.getString("lastName"));
				 results.append("\",\n\"phone\": \"");
				 results.append(rows.getString("phone"));
				 results.append("\",\n\"email\": \"");
				 results.append(rows.getString("email"));
				 results.append("\",\n\"streetAddress\": \"");
				 results.append(rows.getString("streetAddress"));
				 results.append("\",\n\"cityName\": \"");
				 results.append(rows.getString("cityName"));
				 results.append("\",\n\"state\": \"");
				 results.append(rows.getString("state"));
				 results.append("\",\n\"country\": \"");
				 results.append(rows.getString("country"));
				 results.append("\",\n\"zipCode\": \"");
				 results.append(rows.getString("zipCode"));
				 results.append("\"}");
			 }
			 results.append("]}");
			 /* Conclude Connection */
			 conn.close();
			 
			 /* Return Results Back to Caller */
			 return results.toString();
		 }
		 catch (SQLException e) {
			 /* Return Error as JSON Formatted String */
			 return "{ error: \"" + e.getMessage() + "<br>" + connectionString + "\"}";
		 }
 	}

	 public String updatePatient(String source) throws JSONException {
		 try {

			 /* Parse JSON */
			 JSONObject json = new JSONObject(source);
			 JSONObject data = json.getJSONObject("data");
			 
			 /* Attempt to connect to database */
			 Connection conn = DriverManager.getConnection(connectionString);
			 
			 /* Prepare SQL for Execution - Note that each input parameter is marked with a question mark*/
			 String sql = "UPDATE Patient SET lastName = ? WHERE PatientNumber = ?";
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 
			 /* Provide a value for each input parameter */
			 stmt.setString(1, data.getString("lastName"));
			 stmt.setInt(2, data.getInt("patientNumber"));

			 /* Perform Query Operation */
			 int count = stmt.executeUpdate();
			 conn.commit();
			 
			 /* Conclude Connection */
			 conn.close();
			 
			 /* Return Results Back to Caller - how many rows updated*/
			 return "{\"count\":" + Integer.toString(count) + "}";
		 }
		 catch (SQLException e) {
			 /* Return Error as JSON Formatted String */
			 return "{ error: \"" + e.getMessage() + "<br>" + connectionString + "\"}";
		 }
 	}

}
