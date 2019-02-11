package com.revature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.JDBCBank.UserAccounts;
import com.revature.JDBCBank.UserInfo;
import com.revature.util.ConnectionUtil;

public class UserOracle implements UserDao{

	private static UserOracle instance;
	private UserOracle () {}
	public static UserDao getDao() {
		if (instance == null) {
			instance = new UserOracle();
		}
		return instance;
	}
	@Override
	public Optional<List<UserInfo>> getAllUsers() {
		Connection connect = ConnectionUtil.getConnection();
		
		if (connect == null) {
			return Optional.empty();
		}
		try {
			String sql = "select * from users where moderator = 0 order by username";
			PreparedStatement ps = connect.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<UserInfo> allUsers = new ArrayList<UserInfo>();
			while (rs.next()) {
				allUsers.add(new UserInfo(rs.getInt("login_id"), rs.getString("username"), 
						"**********", rs.getString("firstname"), rs.getString("lastname"), rs.getString("email"), rs.getInt("moderator")));
			}
			return Optional.of(allUsers);
		} catch (Exception e) {
			System.out.println("Error from within database.");
			return Optional.empty();
		}
	}

	@Override
	public Optional<UserInfo> deleteUser(Integer id) {
		Connection connect = ConnectionUtil.getConnection();
		
		if (connect == null) {
			return Optional.empty();
		}
		try {
			String sql = "delete from accounts where account_owner = (select username from users where login_id = ?)";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeQuery();
			String sql2 = "delete from users where login_id = ?";
			ps = connect.prepareStatement(sql2);
			ps.setInt(1, id);
			ps.executeQuery();
			String commit = "commit";
			ps = connect.prepareStatement(commit);
			ps.executeQuery();
			System.out.println("User and all affiliated accounts have been deleted.");
			return Optional.empty();
		} catch (Exception e) {
			System.out.println("Error.");
			return Optional.empty();
		}
	}

	@Override
	public Optional<UserInfo> createNewUser(String username, String password, String firstname, String lastname,
			String email, Integer mod) {
		Connection connect = ConnectionUtil.getConnection();
		
		if (connect == null) {
			return Optional.empty();
		}
		try {
			String sql = "call createNewUser(?, ?, ?, ?, ?, ?, ?)";
			CallableStatement cs = connect.prepareCall(sql);
			cs.setString(1, username);
			cs.setString(2, password);
			cs.setString(3, firstname);
			cs.setString(4, lastname);
			cs.setString(5, email);
			cs.setInt(6, 0);
			cs.registerOutParameter(7, Types.INTEGER);
			cs.execute();
			Integer id = cs.getInt(7);
			UserInfo user = new UserInfo(id, username, "*****", firstname, lastname, email, 0);
			System.out.println("Welcome " + username + " to JDBC Online Banking! Be sure to save your login information somewhere secure!");
			return Optional.of(user);
		} catch (Exception e) {
			System.out.println("Username already registered. Use the Member login feature to log in.");
			return Optional.empty();
		}
	}

	@Override
	public Optional<Boolean> logIn(String username, String password, Integer moderator) {
		Connection connect = ConnectionUtil.getConnection();
		
		if (connect == null) {
			return Optional.empty();
		}
		try {
			String sql = "select * from users where username = ? and pass_word = ? and moderator = ?";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setInt(3, moderator);
			ResultSet login = ps.executeQuery();
			List<Integer> foundUser = new ArrayList<Integer>();
			while (login.next()) {
				foundUser.add(new Integer(login.getInt("moderator")));
			}
			if (foundUser.isEmpty()) {
				System.out.println("Incorrect username/password. Returning to main...");
				return Optional.of(false);
			} else {
				return Optional.of(true);
			}
		} catch (Exception e) {
			System.out.println("Error.");
			return Optional.of(false);
		}
	}

	@Override
	public Optional<List<UserAccounts>> getUserAccounts(String username) {
		Connection connect = ConnectionUtil.getConnection();
		
		if (connect == null) {
			return Optional.empty();
		}
		try {
			String sql = "select * from accounts where account_owner = ? order by account_id";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			List<UserAccounts> allAccounts = new ArrayList<UserAccounts>();
			while (rs.next()) {
				allAccounts.add(new UserAccounts(rs.getInt("account_id"), rs.getString("account_name"), 
						rs.getDouble("balance")));
			}
			return Optional.of(allAccounts);
		} catch (Exception e) {
			System.out.println("Error from within database.");
			return Optional.empty();
		}
	}

	@Override
	public Optional<UserAccounts> createNewAccount(String name, String username) {
		Connection connect = ConnectionUtil.getConnection();
		
		if (connect == null) {
			return Optional.empty();
		}
		try {
			String sql = "call createNewAccount(?, ?, ?, ?)";
			CallableStatement cs = connect.prepareCall(sql);
			cs.setString(1, name);
			cs.setString(2, username);
			cs.setInt(3, 0);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.execute();
			Integer id = cs.getInt(4);
			UserAccounts user = new UserAccounts(id, name, 0.0);
			System.out.println("New account " + name + " has been created!");
			return Optional.of(user);
		} catch (Exception e) {
			System.out.println("Could not create new account. Please try again.");
			return Optional.empty();
		}
	}

	@Override
	public Optional<UserAccounts> deleteAccount(Integer id, String username) {
Connection connect = ConnectionUtil.getConnection();
		
		if (connect == null) {
			return Optional.empty();
		}
		try {
			String checkFunds = "select balance from accounts where account_id = ? and account_owner = ?";
			PreparedStatement ps = connect.prepareStatement(checkFunds);
			ps.setInt(1, id);
			ps.setString(2, username);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Double checkBalance = rs.getDouble(1);
			if (checkBalance != 0.0) {
				throw new Exception("Residual Funds Error");
			}
			String sql = "delete from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setString(2, username);
			ps.executeQuery();
			System.out.println("Account " + id + " has been successfully closed.");
			String commit = "commit";
			ps = connect.prepareStatement(commit);
			ps.executeQuery();
			return Optional.empty();
		} catch (SQLException e) {
			System.out.println("You do not own the selected account. Access denied.");
			return Optional.empty();
		} catch (Exception e) {
			System.out.println("Account still has funds available. Please use the withdraw feature to empty account before closure.");
			return Optional.empty();
		}
	}

	@Override
	public Optional<UserAccounts> depositAccount(Integer id, Double balance, String username) {
		Connection connect = ConnectionUtil.getConnection();
		
		if (connect == null) {
			return Optional.empty();
		}
		try {
			String sql = "update accounts set balance = ? + (select balance from accounts where account_id = ?) where account_id = ? and account_owner = ?";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setDouble(1, balance);
			ps.setInt(2, id);
			ps.setInt(3, id);
			ps.setString(4, username);
			ps.executeQuery();
			String sql2 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql2);
			ps.setInt(1, id);
			ps.setString(2, username);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Double newBalance = rs.getDouble(1);
			System.out.println("New account balance: $" + newBalance);
			String commit = "commit";
			ps = connect.prepareStatement(commit);
			ps.executeQuery();
			return Optional.empty();
		} catch (Exception e) {
			System.out.println("You do not own the selected account. Access denied.");
			return Optional.empty();
		}
	}

	@Override
	public Optional<UserAccounts> withdrawAccount(Integer id, Double balance, String username) {
		Connection connect = ConnectionUtil.getConnection();
		
		if (connect == null) {
			return Optional.empty();
		}
		try {
			String checkFunds = "select balance from accounts where account_id = ? and account_owner = ?";
			PreparedStatement ps = connect.prepareStatement(checkFunds);
			ps.setInt(1, id);
			ps.setString(2, username);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Double checkBalance = rs.getDouble(1);
			if (checkBalance < balance) {
				throw new Exception("Overdraft Error");
			}
			String sql = "update accounts set balance = -? + (select balance from accounts where account_id = ?) where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql);
			ps.setDouble(1, balance);
			ps.setInt(2, id);
			ps.setInt(3, id);
			ps.setString(4, username);
			ps.executeQuery();
			String sql2 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql2);
			ps.setInt(1, id);
			ps.setString(2, username);
			rs = ps.executeQuery();
			rs.next();
			Double newBalance = rs.getDouble(1);
			System.out.println("New account balance: $" + newBalance);
			String commit = "commit";
			ps = connect.prepareStatement(commit);
			ps.executeQuery();
			return Optional.empty();
		} catch (SQLException e) {
			System.out.println("You do not own the selected account. Access denied.");
			return Optional.empty();
		} catch (Exception e) {
			System.out.println("Overdraft Error: Account has insufficient funds.");
			return Optional.empty();
		}
	}

	@Override
	public Optional<UserAccounts> transferBetweenAccounts(Integer id, Double balance, Integer id2, String username) {
		Connection connect = ConnectionUtil.getConnection();
		
		if (connect == null) {
			return Optional.empty();
		}
		try {
			String checkFunds = "select balance from accounts where account_id = ? and account_owner = ?";
			PreparedStatement ps = connect.prepareStatement(checkFunds);
			ps.setInt(1, id);
			ps.setString(2, username);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Double checkBalance = rs.getDouble(1);
			if (checkBalance < balance) {
				throw new Exception("Overdraft Error");
			}
			String sql = "update accounts set balance = -? + (select balance from accounts where account_id = ?) where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql);
			ps.setDouble(1, balance);
			ps.setInt(2, id);
			ps.setInt(3, id);
			ps.setString(4, username);
			ps.executeQuery();
			String sql2 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql2);
			ps.setInt(1, id);
			ps.setString(2, username);
			rs = ps.executeQuery();
			rs.next();
			Double newBalance1 = rs.getDouble(1);
			System.out.println("New balance of account_id " + id + ": $" + newBalance1);
			String sql3 = "update accounts set balance = ? + (select balance from accounts where account_id = ?) where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql3);
			ps.setDouble(1, balance);
			ps.setInt(2, id2);
			ps.setInt(3, id2);
			ps.setString(4, username);
			ps.executeQuery();
			String sql4 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql4);
			ps.setInt(1, id2);
			ps.setString(2, username);
			rs = ps.executeQuery();
			rs.next();
			Double newBalance2 = rs.getDouble(1);
			System.out.println("New balance of account_id " + id2 + ": $" + newBalance2);
			String commit = "commit";
			ps = connect.prepareStatement(commit);
			ps.executeQuery();
			return Optional.empty();
		} catch (SQLException e) {
			System.out.println("You do not own the selected account. Access denied.");
			return Optional.empty();
		} catch (Exception e) {
			System.out.println("Overdraft Error: Account " + id + " has insufficient funds.");
			return Optional.empty();
		}
	}
	@Override
	public Optional<UserInfo> modifyUser(Integer id, String firstname, String lastname, String email) {
		Connection connect = ConnectionUtil.getConnection();
		
		if (connect == null) {
			return Optional.empty();
		}
		try {
			String sql = "update users set firstname = ?, lastname = ?, email = ? where login_id = ?";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, firstname);
			ps.setString(2, lastname);
			ps.setString(3, email);
			ps.setInt(4, id);
			ps.executeQuery();
			System.out.println("Account id# " + id + " updated to: \n"
					+ "First name: " + firstname + " \n"
					+ "Last name: " + lastname + " \n"
					+ "Email address: " + email);
			return Optional.empty();
		} catch (Exception e) {
			System.out.println("Error.");
			return Optional.empty();
		}
	}

}
