package test1;

import java.sql.*;

import java.io.*;
// Example 5 : Typical Transaction Program
class JDBC_Test {
	public static void main(String[] args) throws SQLException, IOException{
		// Load MySQL JDBC Driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Could not load the driver");
		}
		
		System.out.println("DB Connection");
		
		//Connect to the database
		String db, user, pass;
		/*
		db = readEntry("DB : ");
		user = readEntry("userid : ");
		pass = readEntry("password: ");
		*/
		db="dbName";
		user="userID";
		pass="userPW";
		Connection conn = DriverManager.getConnection("jdbc:mysql://db.hufs.ac.kr:3306/"+db, user, pass);

		Statement s = conn.createStatement();
		s.executeUpdate("drop table if exists ttt");
		s.executeUpdate("create table ttt(id int primary key) engine=InnoDB");
		boolean success = false;
		conn.setAutoCommit(false);
		// when transactio nis executed normally, set success to true
		// if success is true, commit; otherwise rollback
		try {
			// transaction starts here
			s.executeUpdate("insert into ttt values(1)");
			s.executeUpdate("insert into ttt values(2)");
			//s.executeUpdate("insert into ttt values(1)");
			s.executeUpdate("insert into ttt values(3)");
			success = true;
		} catch(Exception e) {
			System.out.println("Exception occurred. Transaction will be rollbacked");
		} finally {
			try {
				if(success) {
					conn.commit();
				} else {
					conn.rollback();
				}
			} catch(SQLException sqle) {
				sqle.printStackTrace();
			}
			s.close();
			conn.close();
		}
	}
	
	// readEntry function -- to read input string
	static String readEntry(String prompt) {
		try {
			StringBuffer buffer = new StringBuffer();
			System.out.print(prompt);
			System.out.flush();
			int c = System.in.read();
			while (c != '\n' && c != -1) {
				buffer.append((char) c);
				c = System.in.read();
			}
			return buffer.toString().trim();
		} catch (IOException e) {
			return "";
		}
	}
}
