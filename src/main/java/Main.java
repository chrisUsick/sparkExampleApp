import static spark.Spark.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.fasterxml.jackson.*;

public class Main {
	private static Connection c = null;
	private static ObjectMapper om = new ObjectMapper();
	public static void main(String[] args) {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			
			get("/assignments", (req, res) -> {
				List<Assignment> rs = Assignment.all(c);
				List<String> jsonAsgns = new ArrayList();
				for (Assignment a : rs) {
					jsonAsgns.add(om.writeValueAsString(a));
				}
				return om.writeValueAsString(jsonAsgns);
			});
			
			post("/assignments", (req, res) -> {
				try {
					Assignment a = om.readValue(req.body(), Assignment.class);
					
					Validator<Assignment> validator = new Validator<Assignment>();
					Validation val = validator.validate(a, (assign) -> {
						boolean isValid = 
								(assign.name != null && assign.expectedDuration != 0);
						String msg = "Missing Fields: ";
						if (assign.name == null) {
							msg += "name ";
						}
						if (assign.expectedDuration == 0) {
							msg += "expectedDuration";
						}
						return new Validation(isValid, msg);
					});
					if (val.isValid()) {
						boolean success = a.save(c);
						if (success) {
							return om.writeValueAsString(a);
						} else {
							return om.writeValueAsString(
									new ErrorResponse("failed to save"));
						}
					}
					else {
						throw new JsonMissingParam(val.getReason());
					}
				} catch (JsonParseException e) {
					res.status(401);
					return om.writeValueAsString(
							new ErrorResponse("invalid JSON: " + e.getMessage()));
				} catch (JsonMappingException e) {
					return om.writeValueAsString(
							new ErrorResponse("invalid fields: " + e.getMessage()));
				} catch (JsonMissingParam e) {
					return om.writeValueAsString(
							new ErrorResponse("missing parameters: " + e.getMessage()));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
