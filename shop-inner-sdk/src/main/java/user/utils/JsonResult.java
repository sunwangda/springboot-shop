package user.utils;

/**
 * 表示控制器响应的操作结果
 * 
 * @param <T> 向客户端响应的数据的类型
 */

public class JsonResult {

	private Integer state;
	private String message;
	private Object data;

	public JsonResult() {
		super();
	}

	public JsonResult(Integer state) {
		super();
		this.state = state;
	}

	public JsonResult(Throwable ex) {
		super();
		this.message = ex.getMessage();
	}

	public JsonResult(Integer state, Object data) {
		super();
		this.state = state;
		this.data = data;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}