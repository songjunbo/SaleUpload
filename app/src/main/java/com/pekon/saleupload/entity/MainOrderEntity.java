package com.pekon.saleupload.entity;

import android.app.Service;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MainOrderEntity {
	@Id(autoincrement = true)
	private Long id;
	private String billCode;  //单号
	private String saleTime;  //销售时间
	private String saleType;  //交易类型
	private double amount;  //金额
	private double quantity;  //数量

	@Generated(hash = 978762602)
	public MainOrderEntity(Long id, String billCode, String saleTime,
			String saleType, double amount, double quantity) {
		this.id = id;
		this.billCode = billCode;
		this.saleTime = saleTime;
		this.saleType = saleType;
		this.amount = amount;
		this.quantity = quantity;
	}

	@Generated(hash = 746309234)
	public MainOrderEntity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(String saleTime) {
		this.saleTime = saleTime;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
}
