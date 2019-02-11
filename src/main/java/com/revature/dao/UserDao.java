package com.revature.dao;

import java.util.List;
import java.util.Optional;

import com.revature.JDBCBank.UserAccounts;
import com.revature.JDBCBank.UserInfo;

public interface UserDao {
	Optional<List<UserInfo>> getAllUsers();
	Optional<UserInfo> deleteUser(Integer id);
	Optional<UserInfo> createNewUser(String username, String password, String firstname, String lastname, String email, Integer mod);
	Optional<Boolean> logIn(String username, String password, Integer moderator);
	Optional<List<UserAccounts>> getUserAccounts(String username);
	Optional<UserAccounts> createNewAccount(String name, String username);
	Optional<UserAccounts> deleteAccount(Integer id, String username);
	Optional<UserAccounts> depositAccount(Integer id, Double balance, String username);
	Optional<UserAccounts> withdrawAccount(Integer id, Double balance, String username);
	Optional<UserAccounts> transferBetweenAccounts(Integer id, Double balance, Integer id2, String username);
	Optional<UserInfo> modifyUser(Integer id, String firstname, String lastname, String email);
}
