package com.revature.JDBCBank;

import java.io.Serializable;

public class UserAccounts implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4661908671980183566L;
	private Integer account_id;
	private String name;
	private Double balance;
	public UserAccounts() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserAccounts(Integer account_id, String name, Double balance) {
		super();
		this.account_id = account_id;
		this.name = name;
		this.balance = balance;
	}
	public Integer getAccount_id() {
		return account_id;
	}
	public void setAccount_id(Integer account_id) {
		this.account_id = account_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "UserAccounts [account_id=" + account_id + ", name=" + name + ", balance=" + balance + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account_id == null) ? 0 : account_id.hashCode());
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAccounts other = (UserAccounts) obj;
		if (account_id == null) {
			if (other.account_id != null)
				return false;
		} else if (!account_id.equals(other.account_id))
			return false;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}