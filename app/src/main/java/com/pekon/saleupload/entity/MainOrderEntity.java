package com.pekon.saleupload.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MainOrderEntity {
	@Id(autoincrement = true)
	Long id;
	String billId;
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Generated(hash = 325156831)
	public MainOrderEntity(Long id, String billId, String name) {
		this.id = id;
		this.billId = billId;
		this.name = name;
	}

	@Generated(hash = 746309234)
	public MainOrderEntity() {
	}
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBillId() {
		return this.billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	
}
