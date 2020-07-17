package com.cofco.sys.entity.organ;
import com.cofco.base.entity.BaseEntity;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;


/**
 * 用户借调表
 * 
 * @author liming
 * @date 2018-04-27 10:41:38
 */
public class LendEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	//主键
	private String Id;
	//用户id
	private String user_Id;
	//原旧机构ID
	private String old_organ_Id;
	//借调新机构ID
	private String new_organ_Id;
	//借调时间
	private Date lend_time;
	//状态
	private String status;
	//创建时间
	private String create_time;
	//创建人
	private String create_id;
	//用户Id List
	private List<String> userIdList;

	//原机构名称
	private String old_organ_Name;
	//借调机构名称
	private String new_organ_Name;

	public String getOld_organ_Name() {
		return old_organ_Name;
	}

	public void setOld_organ_Name(String old_organ_Name) {
		this.old_organ_Name = old_organ_Name;
	}

	public String getNew_organ_Name() {
		return new_organ_Name;
	}

	public void setNew_organ_Name(String new_organ_Name) {
		this.new_organ_Name = new_organ_Name;
	}

	public List<String> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<String> userIdList) {
		this.userIdList = userIdList;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getUser_Id() {
		return user_Id;
	}

	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}

	public String getOld_organ_Id() {
		return old_organ_Id;
	}

	public void setOld_organ_Id(String old_organ_Id) {
		this.old_organ_Id = old_organ_Id;
	}

	public String getNew_organ_Id() {
		return new_organ_Id;
	}

	public void setNew_organ_Id(String new_organ_Id) {
		this.new_organ_Id = new_organ_Id;
	}

	public Date getLend_time() {
		return lend_time;
	}

	public void setLend_time(Date lend_time) {
		this.lend_time = lend_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCreate_id() {
		return create_id;
	}

	public void setCreate_id(String create_id) {
		this.create_id = create_id;
	}
}
