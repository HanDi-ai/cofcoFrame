package com.cofco.sys.entity.config;


import org.hibernate.validator.constraints.NotBlank;

/**
 * 类SysConfigEntity的功能描述:
 * 系统配置信息
 * @auther cofco
 * @date 2017-08-25 16:19:13
 */
public class SysConfigEntity {
	private String id;
	@NotBlank(message="参数名不能为空")
	private String key;
	@NotBlank(message="参数值不能为空")
	private String value;
	private String status;
	private String remark;

	public String getId() {return id; }
	public void setId(String id) {this.id = id; }
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
}
