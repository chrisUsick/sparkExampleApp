
public class Validation<T> {
	private final boolean isValid;
	private final String reason;
	public Validation (boolean isValid, String reason) {
		this.isValid = isValid;
		this.reason = reason;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public String getReason() {
		return reason;
	}
}