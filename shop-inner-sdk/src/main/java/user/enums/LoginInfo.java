package user.enums;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 登录实体
 */
public class LoginInfo implements Serializable{
	
	/**
	 * 
	 * 登录类型枚举
	 *
	 */
	
	public static enum LoginTypeEnum {
        ADMIN_LOGIN("adminLogin"),
        WX_LOGIN("wxLogin"),
		QQ_LOGIN("qqLogin");
        
		private String loginType;

		private LoginTypeEnum(String loginType) {
			this.loginType = loginType;
		}

		public String getLoginType() {
			return loginType;
		}

		public static LoginTypeEnum getLoginTypeEnum(String loginType) {
			for(LoginTypeEnum item : LoginTypeEnum.values()) {
				if(item.loginType.equals(loginType)) {
					return item;
				}
			}
			return null;
		}
		
    }
	
}
