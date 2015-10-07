import static spark.Spark.*;

import java.sql.*;

import javax.swing.undo.StateEdit;

public class Main {
	private static Connection c = null;
	public static void main(String[] args) {
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			
			get("/assignments", (req, res) -> {
				ResultSet rs = getAssignments();
				return "Hello World";
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static ResultSet getAssignments () {
		try {
			Statement s = c.createStatement();
			String sql = "select * from assignments";
			return s.executeQuery(sql);
		} catch (Exception e){
			return null;
		}
		
		
	}

}
