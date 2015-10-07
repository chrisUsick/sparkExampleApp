package createDatabase;
import java.sql.*;

public class CreateDb {
	private static Connection c = null;
	public static void main(String[] args) {
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:test.db");
	      
//	      System.out.println((checkForTable(c) ? "table exists" : "nope");
	      dropTables();
	      createTables();
	    } catch (SQLException e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    }catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }

	}
	
	private static void dropTables() throws SQLException {
		Statement stmt = c.createStatement();
		
		String submissionsSql = "drop table submissions;";
		String assignmentSql = " drop table assignments;";
		try {
			stmt.executeUpdate(submissionsSql);
		} catch (SQLException e) {
			
		}
		try {
			stmt.executeUpdate(assignmentSql);
		} catch (SQLException e) {
			
		}
		
		stmt.close();
		System.out.println("attempted to drop tables: success");
	}

	public static boolean checkForTable (Connection c) {
		Statement stmt;
		boolean returnVal = false;
		try {
			String sql = "select * from assignments";
			stmt = c.createStatement();
			stmt.executeQuery(sql);
			returnVal = true;
			stmt.close();
		} catch (Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		
		return returnVal;
	}
	public static void createTables () throws SQLException {
		Statement stmt = c.createStatement();
		
		String sql = "create table assignments" +
					 "(assignmentId integer primary key autoincrement not null," + 
					 "name TEXT NOT null," + 
					 "expectedDuration int not null);";
		
		String submissionsSql = "create table submissions" +
					 "(submissionId integer primary key autoincrement not null,"+
					 "duration int not null," +
					 "assignmentId integer not null," + 
					 "FOREIGN key(assignmentId) references assignments(assignmentId))";
		stmt.executeUpdate(sql);
		stmt.executeUpdate(submissionsSql);
		stmt.close();
		
		System.out.println("tables created");
	}

}
