import java.sql.*;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.*;

public class Assignment {
	public int assignmentId;
	public String name;
	public int expectedDuration;
	
	@JsonCreator
	public Assignment (
			@JsonProperty("assignmentId") int id, 
			@JsonProperty("name") String name, 
			@JsonProperty("expectedDuration") int duration) {
		assignmentId = id;
		this.name = name;
		expectedDuration = duration;
	}
	
	public boolean save(Connection c) {
		PreparedStatement stmt;
		boolean returnVal = false;
		try {
			String sql = "INSERT INTO assignments (name, expectedDuration)" +
					"VALUES (:name, :duration)";
			stmt = c.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setInt(2, expectedDuration);
			stmt.executeUpdate();
			returnVal = true;
			ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid() as id");
			
			// get the first row
			rs.next();
			assignmentId = rs.getInt(0);
			stmt.close();
		} catch (Exception e){
		}
		return returnVal;
	}
	
	public static ArrayList<Assignment> all(Connection c) {

		ArrayList<Assignment> out = new ArrayList<>();
		try {
			Statement s = c.createStatement();
			String sql = "select assignmentId, name, expectedDuration from assignments";
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				out.add(new Assignment(
						rs.getInt(1), rs.getString(2), rs.getInt(3)));
			}
			return out;
		} catch (Exception e){
			return null;
		}
	}
	
	
}
