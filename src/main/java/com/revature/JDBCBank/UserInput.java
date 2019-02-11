package com.revature.JDBCBank;

import java.util.Scanner;

public class UserInput {
	@SuppressWarnings("resource")
	static int getInput() {
		boolean check = false;
		int i = 0;
		while (check != true) {
			try {
				check = true;
				Scanner user = new Scanner(System.in);
				i = user.nextInt();	
			} catch(Exception e) {
				System.out.println("----------------------------------------------");
				System.out.println("Input only accepts numbers (0-9). Please try again...");
				check = false;
				System.out.println("----------------------------------------------");
			}
		}
	return i;
	}
	
	@SuppressWarnings("resource")
	static String getStringInput() {
		String s = new String();
		Scanner string = new Scanner(System.in);
		s = string.nextLine();
		return s;
	}
	
	@SuppressWarnings("resource")
	static double getBalance() {
		boolean check = false;
		double i = 0;
		while (check != true) {
			try {
				check = true;
				Scanner user = new Scanner(System.in);
				i = user.nextDouble();	
			} catch(Exception e) {
				System.out.println("----------------------------------------------");
				System.out.println("Input only accepts withdrawls/deposits in the form of 'xx.xx'. Please try again...");
				check = false;
				System.out.println("----------------------------------------------");
			}
		}
	return i;
	}
}
