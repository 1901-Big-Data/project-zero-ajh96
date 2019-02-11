package com.revature.services;

import java.util.List;
import java.util.Optional;

import com.revature.JDBCBank.UserAccounts;
import com.revature.JDBCBank.UserInfo;
import com.revature.dao.UserDao;
import com.revature.dao.UserOracle;

public class UserServices {
	private static UserServices userservice;
	final static UserDao userDao = UserOracle.getDao();
	public static UserServices getServices() {
		if (userservice == null) {
			userservice = new UserServices();
		}
	return userservice;
	}
	
	public Optional<List<UserInfo>> getUsers() {
		return userDao.getAllUsers();
	}
	
	
	public Optional<UserInfo> deleteUsers(Integer id) {
		return userDao.deleteUser(id);
	}

	public Optional<UserInfo> createNewUsers(String username, String password, String firstname, String lastname,
			String email, Integer mod) {
		return userDao.createNewUser(username, password, firstname, lastname, email, mod);
	}

	public Optional<Boolean> logIntoBank(String username, String password, Integer moderator) {
		return userDao.logIn(username, password, moderator);
	}

	public Optional<List<UserAccounts>> getAllAccounts(String username) {
		return userDao.getUserAccounts(username);
	}

	public Optional<UserAccounts> createNewBankAccounts(String name, String username) {
		return userDao.createNewAccount(name, username);
	}

	public Optional<UserAccounts> deleteBankAccount(Integer id, String username) {
		return userDao.deleteAccount(id, username);
	}

	public Optional<UserAccounts> depositIntoAccount(Integer id, Double balance, String username) {
		return userDao.depositAccount(id, balance, username);
	}

	public Optional<UserAccounts> withdrawFromAccount(Integer id, Double balance, String username) {
		return userDao.withdrawAccount(id, balance, username);
	}

	public Optional<UserAccounts> transferBetweenTwoAccounts(Integer id, Double balance, Integer id2, String username) {
		return userDao.transferBetweenAccounts(id, balance, id2, username);
	}
	
	public Optional<UserInfo> modifyUsers(Integer id, String firstname, String lastname, String email) {
		return userDao.modifyUser(id, firstname, lastname, email);
	}
}
