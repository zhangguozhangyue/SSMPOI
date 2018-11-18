package cn.zg.request;

import java.io.Serializable;

public class StudentRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6235211737642357039L;

	private Integer id;

    private String sex;

    private String university;
    
    private String college;

    public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	private Integer year;

    private Integer jvalue;

   /* private String remark;*/

    /*@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date purchaseDate;*/
    
    private Integer isDelete;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getJvalue() {
		return jvalue;
	}

	public void setJvalue(Integer jvalue) {
		this.jvalue = jvalue;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", sex=" + sex + ", university=" + university + ", year=" + year + ", jvalue="
				+ jvalue + ", isDelete=" + isDelete + "]";
	}

   

   /* public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }*/

	/*public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}*/

	

}
