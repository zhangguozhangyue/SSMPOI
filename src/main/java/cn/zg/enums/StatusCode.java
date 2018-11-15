package cn.zg.enums;


public enum StatusCode {

	Success(0,"成功"),
	Fail(-1,"失败"),
	Invalid_Param(1001,"无效的参数"),
	System_Error(1002,"系统错误"),
	Entity_Not_Exist(1003,"对象实体不存在"),
	
	WorkBook_Version_Invalid(1003,"excel版本号不合法!");
	
	private Integer code;
	private String msg;
	
	private StatusCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}

