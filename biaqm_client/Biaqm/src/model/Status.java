package model;

public class Status 
{
	private boolean Successeded;
	private String Token;
    private String Message ;
    
    
	public Status(boolean successeded, String token, String message) {
		super();
		Successeded = successeded;
		Token = token;
		Message = message;
	}
	
	
	
	@Override
	public String toString() {
		return "Status [Successeded=" + Successeded + ", Token=" + Token
				+ ", Message=" + Message + "]";
	}



	public boolean isSuccesseded() {
		return Successeded;
	}
	public void setSuccesseded(boolean successeded) {
		Successeded = successeded;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
    
    
}
