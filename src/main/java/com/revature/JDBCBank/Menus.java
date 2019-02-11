package com.revature.JDBCBank;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.revature.services.UserServices;

public class Menus extends UserInput{
	static void openMenu() {
		Integer input = 0;
		while (input != 9) {
			System.out.println("Welcome to JDBC Online Banking! Please select an option: \n" 
					+ "1. Log into existing account.\n"
					+ "2. Create new account.\n"
					+ "9. Exit. \n"
					+ "----------------------------------------------");
			input = getInput();
			System.out.println("----------------------------------------------");
			if (input == 1) {
				logIn();
			} else if (input == 2) {
				createNewUser();
			} else if (input == 9) {
				break;
			} else {
				System.out.println("Menu doesn't exist. Reloading main menu...");
				System.out.println("----------------------------------------------");
			}
		}
		System.out.println("Thank you for using JDBC Online Banking! Have a great day!");
	}
	
	static void createNewUser() {
		System.out.println("Enter new username: ");
		String username = getStringInput();
		System.out.println("Enter new password: ");
		String password = getStringInput();
		System.out.println("Enter your first name: ");
		String firstname = getStringInput();
		System.out.println("Enter your last name: ");
		String lastname = getStringInput();
		System.out.println("Enter your email address: ");
		String email = getStringInput();
		try {
			UserServices.getServices().createNewUsers(username, password, firstname, lastname, email, 0);
		} catch (NoSuchElementException e) {
			System.out.println("Error.");
		}
		
	}
	
	static void logIn() {
		System.out.println("Login homepage. Please select an option: \n" + "1. Member login. \n" + "2. Moderator login. \n"
				+ "9. Back to main menu.");
		String username = new String();
		String password = new String();
		Integer input = getInput();
		if (input == 10) {
			input = 0;
		}
		System.out.println("----------------------------------------------");
		while (input != 10) {
			if (input == 1) {
				System.out.println("Member Login Menu \n" + "Enter username:");
				username = getStringInput();
				System.out.println("Enter password:");
				password = getStringInput();
				System.out.println("Attempting to log in...");
				System.out.println("----------------------------------------------");
				try {
					Optional<Boolean> login = UserServices.getServices().logIntoBank(username, password, 0);
					if (login.get() == true) {
						bankingMenu(username);
					}
				} catch (NoSuchElementException e) {
					System.out.println("Unable to login.");
				}
				break;
			} else if (input == 2) {
				System.out.println("Moderator Login Menu \n" + "Enter username:");
				username = getStringInput();
				System.out.println("Enter password:");
				password = getStringInput();
				System.out.println("Attempting to log in as moderator...");
				System.out.println("----------------------------------------------");
				try {
					Optional<Boolean> login = UserServices.getServices().logIntoBank(username, password, 1);
					if (login.get() == true) {
						modMenu(username);
					}
				} catch (NoSuchElementException e) {
					System.out.println("Unable to login.");
				}
				break;
			} else if (input == 9) {
				System.out.println("Returning to main menu...");
				System.out.println("----------------------------------------------");
				break;
			} else {
				System.out.println("Menu doesn't exist. Please select an option: \n" + "1. Member login. \n" + "2. Moderator login. \n"
						+ "9. Back to main menu.");
				System.out.println("----------------------------------------------");
				input = getInput();
			}
		}
	
	}
	
	static void bankingMenu(String username) {
		System.out.println("Welcome back " + username + "!");
		Integer input = 0;
		while (input != 9) {
			System.out.println("What would you like to do? \n"
					+ "1. View account balances. \n"
					+ "2. Create new account. \n"
					+ "3. Close account. \n"
					+ "4. Deposit into account. \n"
					+ "5. Withdraw from account. \n"
					+ "6. Transfer funds between accounts. \n"
					+ "9. Log out.");
			input = getInput();
			System.out.println("----------------------------------------------");
			if (input == 1) {
				UserServices userService = UserServices.getServices();
				List<UserAccounts> userAcc = new ArrayList<>();
				try {
					userAcc = userService.getAllAccounts(username).get();
					if (userAcc.isEmpty()) {
						System.out.println("You currently have no accounts open. Use option '2' to create a new account.");
					}
					for (int u = 0; u < userAcc.size(); u++) {
						System.out.println(userAcc.get(u));
					}
				} catch (NoSuchElementException e) {
					System.out.println("Failed to fetch accounts.");
				}
			} else if (input == 2) {
				createAccount(username);
			} else if (input == 3) {
				deleteAccount(username);
			} else if (input == 4) {
				depositMenu(username);
			} else if (input == 5) {
				withdrawalMenu(username);
			} else if (input == 6) {
				transferMenu(username);
			} else if (input == 9) {
				System.out.println("Logging out...");
				System.out.println("----------------------------------------------");
				break;
			} else {
				System.out.println("Menu doesn't exist. Choose options 1-6, or type '9' to log out.");
				System.out.println("----------------------------------------------");
			}
		}
	}
	
	static void withdrawalMenu(String username) {
		UserServices userService = UserServices.getServices();
		List<UserAccounts> userAcc = new ArrayList<>();
		try {
			userAcc = userService.getAllAccounts(username).get();
			if (userAcc.isEmpty()) {
				System.out.println("You currently have no accounts open. Use option '2' to create a new account.");
			}
			for (int u = 0; u < userAcc.size(); u++) {
				System.out.println(userAcc.get(u));
			}
			System.out.println("Type the id# of the account you wish to withdraw from: ");
			Integer id = getInput();
			System.out.println("Enter the amount to be withdrawn (format xx.xx): ");
			Double amount = getBalance();
			UserServices.getServices().withdrawFromAccount(id, amount, username);
		} catch (NoSuchElementException e) {
			System.out.println("Failed to fetch accounts.");
		}
	}
	
	static void depositMenu(String username) {
		UserServices userService = UserServices.getServices();
		List<UserAccounts> userAcc = new ArrayList<>();
		try {
			userAcc = userService.getAllAccounts(username).get();
			if (userAcc.isEmpty()) {
				System.out.println("You currently have no accounts open. Use option '2' to create a new account.");
			}
			for (int u = 0; u < userAcc.size(); u++) {
				System.out.println(userAcc.get(u));
			}
			System.out.println("Type the id# of the account you wish to deposit into: ");
			Integer id = getInput();
			System.out.println("Enter the amount to be deposited (format xx.xx): ");
			Double amount = getBalance();
			UserServices.getServices().depositIntoAccount(id, amount, username);
		} catch (NoSuchElementException e) {
			System.out.println("Failed to fetch accounts.");
		}
	}
	
	static void transferMenu(String username) {
		UserServices userService = UserServices.getServices();
		List<UserAccounts> userAcc = new ArrayList<>();
		try {
			userAcc = userService.getAllAccounts(username).get();
			if (userAcc.isEmpty()) {
				System.out.println("You currently have no accounts open. Use option '2' to create a new account.");
			}
			for (int u = 0; u < userAcc.size(); u++) {
				System.out.println(userAcc.get(u));
			}
			System.out.println("Type the id# of the account you wish to transfer from: ");
			Integer id = getInput();
			System.out.println("Enter the amount to be transfered (format xx.xx): ");
			Double amount = getBalance();
			System.out.println("Enter the id# of the account that you wish to deposit into: ");
			Integer id2 = getInput();
			UserServices.getServices().transferBetweenTwoAccounts(id, amount, id2, username);
		} catch (NoSuchElementException e) {
			System.out.println("Failed to fetch accounts.");
		}
	}
	
	static void createAccount(String username) {
		System.out.println("Enter name of new account: ");
		String name = getStringInput();
		try {
			UserServices.getServices().createNewBankAccounts(name, username);
		} catch (NoSuchElementException e) {
			System.out.println("Error.");
		}
		
	}
	
	static void deleteAccount(String username) {
		UserServices userService = UserServices.getServices();
		List<UserAccounts> userAcc = new ArrayList<>();
		try {
			userAcc = userService.getAllAccounts(username).get();
			if (userAcc.isEmpty()) {
				System.out.println("You currently have no accounts open. Use option '2' to create a new account.");
			}
			for (int u = 0; u < userAcc.size(); u++) {
				System.out.println(userAcc.get(u));
			}
			System.out.println("Type the id# of the account you wish to close: ");
			System.out.println("*Note: You cannot close an account that still has available funds remaining.*");
			Integer id = getInput();
			System.out.println("Warning: Account id# " + id + " will be closed. Type 'Confirm' to confirm closure.");
			String confirm = getStringInput();
			if (confirm.equals("Confirm")) {
				UserServices.getServices().deleteBankAccount(id, username);
			} else {
				System.out.println("Closure canceled. Returning to menu...");
			}
		} catch (NoSuchElementException e) {
			System.out.println("Failed to fetch accounts.");
		}
	}
	
	static void modifyUser() {
		UserServices userService = UserServices.getServices();
		List<UserInfo> userInfo = new ArrayList<>();
		try {
			userInfo = userService.getUsers().get();
			for (int u = 0; u < userInfo.size(); u++) {
				System.out.println(userInfo.get(u));
			}
			System.out.println("Input login id of user to be modified: ");
			Integer id = getInput();
			System.out.println("Input new first name: ");
			String firstname = getStringInput();
			System.out.println("Input new last name: ");
			String lastname = getStringInput();
			System.out.println("Input new email address: ");
			String email = getStringInput();
			System.out.println("Warning: Changes made to login id# " + id + " will be permanent. Type 'Confirm' to confirm modifications.");
			String confirmation = getStringInput();
			if (confirmation.equals("Confirm")) {
				UserServices.getServices().modifyUsers(id, firstname, lastname, email);
			} else {
				System.out.println("Modification canceled. Returning to menu...");
			}
		} catch (NoSuchElementException e) {
			System.out.println("Unable to collect user.");
		}
	}
	
	static void deleteUser() {
		UserServices userService = UserServices.getServices();
		List<UserInfo> userInfo = new ArrayList<>();
		try {
			userInfo = userService.getUsers().get();
			for (int u = 0; u < userInfo.size(); u++) {
				System.out.println(userInfo.get(u));
			}
			System.out.println("Input login id of user to be deleted: ");
			Integer id = getInput();
			System.out.println("Warning: termination of login id# " + id + " will also terminate any personal accounts held by that user. Type 'Confirm' to confirm user deletion.");
			String confirmation = getStringInput();
			if (confirmation.equals("Confirm")) {
				UserServices.getServices().deleteUsers(id);
			} else {
				System.out.println("User deletion canceled. Returning to menu...");
			}
		} catch (NoSuchElementException e) {
			System.out.println("Unable to collect user.");
		}
	}
	
	static void modMenu(String username) {
		System.out.println("Moderator: " + username);
		Integer input = 0;
		while (input != 9) {
			System.out.println("Mod menu: \n"
					+ "1. View user database. \n"
					+ "2. Create new user. \n"
					+ "3. Modify existing user. \n"
					+ "4. Delete existing user. \n"
					+ "9. Log out.");
			input = getInput();
			System.out.println("----------------------------------------------");
			if (input == 1) {
				UserServices userService = UserServices.getServices();
				List<UserInfo> userInfo = new ArrayList<>();
				try {
					userInfo = userService.getUsers().get();
					for (int u = 0; u < userInfo.size(); u++) {
						System.out.println(userInfo.get(u));
					}
				} catch (NoSuchElementException e) {
					System.out.println("Unable to collect users.");
				}
			} else if (input == 2) {
				createNewUser();
			} else if (input == 3) {
				modifyUser();
			} else if (input == 4) {
				deleteUser();
			} else if (input == 9) {
				System.out.println("Logging out...");
				System.out.println("----------------------------------------------");
				break;
			} else {
				System.out.println("Menu doesn't exist. Choose options '1-4', or type '9' to log out.");
				System.out.println("----------------------------------------------");
			}
		}
	}
}
