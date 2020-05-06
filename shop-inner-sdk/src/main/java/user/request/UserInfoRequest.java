package user.request;

import io.swagger.annotations.ApiModelProperty;

public class UserInfoRequest extends CommonRequest{
	
	@ApiModelProperty(required = false, value = "账号")
	private String admin;
	@ApiModelProperty(required = false, value = "密码")
	private String password;
	@ApiModelProperty(required = false, value = "用户名")
	private String username;
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
