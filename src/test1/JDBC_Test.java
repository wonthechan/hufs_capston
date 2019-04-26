package test1;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.io.*;
// Example 5 : Typical Transaction Program
class JDBC_Test {
	
	static List<HashMap<String, String>> majorLecList = null;
	static List<HashMap<String, String>> liberalLecList = null;
	
	public static void main(String[] args) throws SQLException, IOException{
		
		LecParseTest lecParseTest = new LecParseTest();
		//majorLecList = lecParseTest.getMajorLecList();
		liberalLecList = lecParseTest.getLiberalLecList();
		
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
		db="capstonDB";
		user="s201402783";
		pass=readEntry("password: ");
		//pass = "";
		Connection conn = DriverManager.getConnection("jdbc:mysql://106.10.42.35:3306/"+db, user, pass);

		String insertQuery = null;
		Statement s = conn.createStatement();
		boolean success = false;
		conn.setAutoCommit(false);
		// when transactio nis executed normally, set success to true
		// if success is true, commit; otherwise rollback
		try {
			// transaction starts here
			System.out.println("Updating capstonDB...");

			/* 전공 강의들을 모두 DB에 Insert */
			/*
			int cntMajor = majorLecList.size();	
			for(int i = 0; i < cntMajor; i++) {
				//System.out.println(majorLecList.get(i).get("title"));
				System.out.println(i);
				insertQuery = "insert into TIMETABLE values (\'";
				insertQuery += majorLecList.get(i).get("code") + "\', \'";
				insertQuery += majorLecList.get(i).get("gubun") + "\', \'";
				insertQuery += majorLecList.get(i).get("area") + "\', \'";
				insertQuery += majorLecList.get(i).get("year") + "\', \'";
				insertQuery += majorLecList.get(i).get("title") + "\', \'";
				insertQuery += majorLecList.get(i).get("prof") + "\', \'";
				insertQuery += majorLecList.get(i).get("credit") + "\', \'";
				insertQuery += majorLecList.get(i).get("time") + "\', \'";
				insertQuery += majorLecList.get(i).get("sched") + "\', \'";
				insertQuery += majorLecList.get(i).get("numpeople").substring(0, majorLecList.get(0).get("numpeople").indexOf('/')-1) + "\', \'";
				insertQuery += majorLecList.get(i).get("numpeople").substring(majorLecList.get(0).get("numpeople").indexOf('/')+1) + "\', \'";
				insertQuery += majorLecList.get(i).get("junpil") + "\', \'";
				insertQuery += majorLecList.get(i).get("cyber") + "\', \'";
				insertQuery += majorLecList.get(i).get("muke") + "\', \'";
				insertQuery += majorLecList.get(i).get("foreign") + "\', \'";
				insertQuery += majorLecList.get(i).get("team") + "\', \'";
				insertQuery += majorLecList.get(i).get("note") + "\')";
				s.executeUpdate(insertQuery);
			}
			System.out.println("Major Insert Task Done!");
			*/
			
			/* 교양 강의들을 모두 DB에 Insert */
			insertQuery = null;
			int cntLiberal = liberalLecList.size();	
			for(int i = 0; i < cntLiberal; i++) {
				//System.out.println(majorLecList.get(i).get("title"));
				System.out.println(i);
				insertQuery = "insert into TIMETABLE values (\'";
				insertQuery += liberalLecList.get(i).get("code") + "\', \'";
				insertQuery += liberalLecList.get(i).get("gubun") + "\', \'";
				insertQuery += liberalLecList.get(i).get("area") + "\', \'";
				insertQuery += liberalLecList.get(i).get("year") + "\', \'";
				insertQuery += liberalLecList.get(i).get("title") + "\', \'";
				insertQuery += liberalLecList.get(i).get("prof") + "\', \'";
				insertQuery += liberalLecList.get(i).get("credit") + "\', \'";
				insertQuery += liberalLecList.get(i).get("time") + "\', \'";
				insertQuery += liberalLecList.get(i).get("sched") + "\', \'";
				insertQuery += liberalLecList.get(i).get("numpeople").substring(0, liberalLecList.get(0).get("numpeople").indexOf('/')-1) + "\', \'";
				insertQuery += liberalLecList.get(i).get("numpeople").substring(liberalLecList.get(0).get("numpeople").indexOf('/')+1) + "\', \'";
				insertQuery += liberalLecList.get(i).get("junpil") + "\', \'";
				insertQuery += liberalLecList.get(i).get("cyber") + "\', \'";
				insertQuery += liberalLecList.get(i).get("muke") + "\', \'";
				insertQuery += liberalLecList.get(i).get("foreign") + "\', \'";
				insertQuery += liberalLecList.get(i).get("team") + "\', \'";
				insertQuery += liberalLecList.get(i).get("note") + "\')";
				s.executeUpdate(insertQuery);
			}
			System.out.println("Liberal Insert Task Done!");
			
			success = true;
		} catch(Exception e) {
			System.out.println("Exception occurred. Transaction will be rollbacked");
			System.out.println(insertQuery);
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