package com.revature.JDBCBank;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.dao.UserDao;
import com.revature.dao.UserOracle;
import com.revature.services.UserServices;
import com.revature.util.ConnectionUtil;

import org.junit.Test;

public class JDBCBankingTest extends UserServices{
	
	private static UserServices userservice;
	final static UserDao userDao = UserOracle.getDao();
	public static UserServices getServices() {
		if (userservice == null) {
			userservice = new UserServices();
		}
	return userservice;
	}

	@Test
	public void testGetAllUsers() {
		assertNotNull(getUsers());
	}

	@Test
	public void testLoginAsMember() {
		Optional<Boolean> login = UserServices.getServices().logIntoBank("ichinisan", "junichi", 0);
		assertTrue(login.get());
	}

	@Test
	public void testLoginAsModerator() {
		Optional<Boolean> login = UserServices.getServices().logIntoBank("MichaelBay5", "Explosion", 1);
		assertTrue(login.get());
	}
	
	@Test
	public void testFailLoginWithWrongUsername() {
		Optional<Boolean> login = UserServices.getServices().logIntoBank("ichirokusan", "junichi", 0);
		assertFalse(login.get());
	}
	
	@Test
	public void testFailLoginWithWrongPassword() {
		Optional<Boolean> login = UserServices.getServices().logIntoBank("ichinisan", "chunichi", 0);
		assertFalse(login.get());
	}
	
	@Test
	public void testFailLoginWithUnregisteredAccount() {
		Optional<Boolean> login = UserServices.getServices().logIntoBank("isweariexist", "logmein", 0);
		assertFalse(login.get());
	}
	
	@Test
	public void testGetAllAccountsofaUser() {
		List<UserAccounts> userAcc = new ArrayList<>();
		userAcc = getAllAccounts("ichinisan").get();
		assertFalse(userAcc.isEmpty());
	}
	
	@Test
	public void testFailtoRetrieveAccountsofaUserWithNoAccounts() {
		List<UserAccounts> userAcc = new ArrayList<>();
		userAcc = getAllAccounts("MichaelBay5").get();
		assertTrue(userAcc.isEmpty());
	}
	
	@Test
	public void testFailToGetAccountofUnregisteredUser() {
		List<UserAccounts> userAcc = new ArrayList<>();
		userAcc = getAllAccounts("iwillmakeausername").get();
		assertTrue(userAcc.isEmpty());
	}
	
	@Test()
	public void testDepositIntoAccount() throws Exception {
		try {
			Connection connect = ConnectionUtil.getConnection();
			String sql = "select balance from accounts where account_id = ? and account_owner = ?";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps = connect.prepareStatement(sql);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			ResultSet rs = ps.executeQuery();
			rs.next();
			Double originalBalance = rs.getDouble(1);
			//tests if I can deposit into account 0 (user "ichinisan" owns this account)
			depositIntoAccount(0, 1.50, "ichinisan");
			//throws exception if account isn't owned by "ichinisan"
			String sql2 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql2);
			ps = connect.prepareStatement(sql2);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double newBalance = rs.getDouble(1);
			assertNotEquals(originalBalance, newBalance);
		} catch (Exception e) {
			throw new Exception("Failed to deposit");
		}
	}
	
	@Test(expected = Exception.class)
	public void testFailDepositIntoUnownedAccount() throws Exception {
		try {
			Connection connect = ConnectionUtil.getConnection();
			String sql = "select balance from accounts where account_id = ? and account_owner = ?";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps = connect.prepareStatement(sql);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			ResultSet rs = ps.executeQuery();
			rs.next();
			Double originalBalance = rs.getDouble(1);
			//tests if I can deposit into account 3
			depositIntoAccount(3, 1.50, "ichinisan");
			//throws exception if account isn't owned by "ichinisan"
			String sql2 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql2);
			ps = connect.prepareStatement(sql2);
			ps.setInt(1, 3);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double newBalance = rs.getDouble(1);
			assertNotEquals(originalBalance, newBalance);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Test
	public void testWithdrawFromAccount() throws Exception {
		try {
			Connection connect = ConnectionUtil.getConnection();
			String sql = "select balance from accounts where account_id = ? and account_owner = ?";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps = connect.prepareStatement(sql);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			ResultSet rs = ps.executeQuery();
			rs.next();
			Double originalBalance = rs.getDouble(1);
			//tests if I can withdraw from account 0 (user "ichinisan" owns this account)
			withdrawFromAccount(0, 1.50, "ichinisan");
			//throws exception if account isn't owned by "ichinisan"
			String sql2 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql2);
			ps = connect.prepareStatement(sql2);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double newBalance = rs.getDouble(1);
			assertNotEquals(originalBalance, newBalance);
		} catch (Exception e) {
			throw new Exception("Failed to withdraw");
		}
	}
	
	@Test(expected = Exception.class)
	public void testFailWithdrawFromUnownedAccount() throws Exception{
		try {
			Connection connect = ConnectionUtil.getConnection();
			String sql = "select balance from accounts where account_id = ? and account_owner = ?";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps = connect.prepareStatement(sql);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			ResultSet rs = ps.executeQuery();
			rs.next();
			Double originalBalance = rs.getDouble(1);
			//tests if I can withdraw from account 4 (user "ichinisan" doesn't own this account)
			withdrawFromAccount(4, 1.50, "ichinisan");
			//throws exception if account isn't owned by "ichinisan"
			String sql2 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql2);
			ps = connect.prepareStatement(sql2);
			ps.setInt(1, 4);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double newBalance = rs.getDouble(1);
			assertNotEquals(originalBalance, newBalance);
		} catch (Exception e) {
			throw new Exception("Failed to withdraw");
		}
	}
	
	@Test(expected = Exception.class)
	public void testFailWithdrawInsufficientFunds() throws Exception{
		try {
			Connection connect = ConnectionUtil.getConnection();
			String sql = "select balance from accounts where account_id = ? and account_owner = ?";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps = connect.prepareStatement(sql);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			ResultSet rs = ps.executeQuery();
			rs.next();
			Double originalBalance = rs.getDouble(1);
			//tests if I can withdraw from account 0 (user "ichinisan" owns this account)
			withdrawFromAccount(0, 15000000000.00, "ichinisan");
			//throws exception if attempting to overdraft
			String sql2 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql2);
			ps = connect.prepareStatement(sql2);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double newBalance = rs.getDouble(1);
			if (originalBalance.equals(newBalance)) {
				throw new Exception();
			}
			assertNotEquals(originalBalance, newBalance);
		} catch (Exception e) {
			throw new Exception("Failed to withdraw");
		}
	}
	
	@Test
	public void testTransferBetweenAccounts() throws Exception {
		try {
			Connection connect = ConnectionUtil.getConnection();
			String sql = "select balance from accounts where account_id = ? and account_owner = ?";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps = connect.prepareStatement(sql);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			ResultSet rs = ps.executeQuery();
			rs.next();
			Double originalBalance = rs.getDouble(1);
			String sql2 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql2);
			ps = connect.prepareStatement(sql2);
			ps.setInt(1, 1);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double originalBalance2 = rs.getDouble(1);
			//tests if I can transfer from account 0  to account 1 (user "ichinisan" owns both accounts)
			transferBetweenTwoAccounts(0, 1.50, 1, "ichinisan");
			//throws exception if accounts are not owned by "ichinisan"
			String sql3 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql3);
			ps = connect.prepareStatement(sql3);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double newBalance = rs.getDouble(1);
			String sql4 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql4);
			ps = connect.prepareStatement(sql4);
			ps.setInt(1, 1);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double newBalance2 = rs.getDouble(1);
			assertNotEquals(originalBalance, newBalance);
			assertNotEquals(originalBalance2, newBalance2);
		} catch (Exception e) {
			throw new Exception("Failed to transfer funds");
		}
	}
	
	@Test(expected = Exception.class)
	public void testFailTransferFromUnownedAccount() throws Exception {
		try {
			Connection connect = ConnectionUtil.getConnection();
			String sql = "select balance from accounts where account_id = ? and account_owner = ?";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps = connect.prepareStatement(sql);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			ResultSet rs = ps.executeQuery();
			rs.next();
			Double originalBalance = rs.getDouble(1);
			String sql2 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql2);
			ps = connect.prepareStatement(sql2);
			ps.setInt(1, 1);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double originalBalance2 = rs.getDouble(1);
			//tests if I can transfer from account 0  to account 4 (user "ichinisan" doesn't own account 4)
			transferBetweenTwoAccounts(0, 1.50, 4, "ichinisan");
			//throws exception if either account is not owned by "ichinisan"
			String sql3 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql3);
			ps = connect.prepareStatement(sql3);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double newBalance = rs.getDouble(1);
			String sql4 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql4);
			ps = connect.prepareStatement(sql4);
			ps.setInt(1, 4);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double newBalance2 = rs.getDouble(1);
			assertNotEquals(originalBalance, newBalance);
			assertNotEquals(originalBalance2, newBalance2);
		} catch (Exception e) {
			throw new Exception("Failed to transfer funds");
		}
	}
	
	@Test(expected = Exception.class)
	public void testFailTransferInsufficientFunds() throws Exception {
		try {
			Connection connect = ConnectionUtil.getConnection();
			String sql = "select balance from accounts where account_id = ? and account_owner = ?";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps = connect.prepareStatement(sql);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			ResultSet rs = ps.executeQuery();
			rs.next();
			Double originalBalance = rs.getDouble(1);
			String sql2 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql2);
			ps = connect.prepareStatement(sql2);
			ps.setInt(1, 1);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double originalBalance2 = rs.getDouble(1);
			//tests if I can transfer from account 0  to account 1 (user "ichinisan" owns both accounts)
			transferBetweenTwoAccounts(0, 15000000000000.00, 1, "ichinisan");
			//throws exception if either account if attempting to overdraft from account 0
			String sql3 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql3);
			ps = connect.prepareStatement(sql3);
			ps.setInt(1, 0);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double newBalance = rs.getDouble(1);
			String sql4 = "select balance from accounts where account_id = ? and account_owner = ?";
			ps = connect.prepareStatement(sql4);
			ps = connect.prepareStatement(sql4);
			ps.setInt(1, 1);
			ps.setString(2, "ichinisan");
			rs = ps.executeQuery();
			rs.next();
			Double newBalance2 = rs.getDouble(1);
			if (originalBalance.equals(newBalance)) {
				throw new Exception();
			}
			assertNotEquals(originalBalance, newBalance);
			assertNotEquals(originalBalance2, newBalance2);
		} catch (Exception e) {
			throw new Exception("Failed to transfer funds");
		}
	}

}
