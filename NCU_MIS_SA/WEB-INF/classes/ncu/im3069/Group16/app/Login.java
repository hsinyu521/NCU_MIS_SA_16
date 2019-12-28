package ncu.im3069.Group16.app;

public class Login {
	private String memberType;	//看是老師還是家長會員
	private String email;
	private String password;
	
	//登入的建構子
	public Login(String memberType, String email, String password) {
		this.memberType = memberType;
		this.email = email;
		this.password = password;
	}
	
	public String getMemberType() {
        return this.memberType;
    }
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPwd() {
		return this.password;
	}
}
