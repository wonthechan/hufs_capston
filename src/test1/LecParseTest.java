package test1;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LecParseTest {

	static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Whale/1.4.64.6 Safari/537.36";
	
	static List<HashMap<String, String>> majorCodeList = null;
	static List<HashMap<String, String>> liberalCodeList = null;
	static List<HashMap<String, String>> majorLecList = null;
	static List<HashMap<String, String>> liberalLecList = null;

	/*
	ag_ledg_year(����) : 2019
	ag_ledg_sessn(�б�) : 1(1�б�), 2(��������), 3(2�б�), 4(�ܿ����)
	ag_org_sect(�Ҽӱ���) : A(�к�), B(���п�) ... 
	campus_sect(ķ�۽� ����) : H1(����), H2(�۷ι�)
	gubun(����/���� ���� ����) : 1(���������� ������ ���), 2(���翵���� ������ ���)
	ag_crs_strct_cd: AQR02_H2
	ag_compt_fld_cd: 303_H2
	*/
	static String param_year;
	static String param_session;
	static String param_orgSect;
	static String param_camSect;
	static String param_gubun;
	static String param_MajorCode;
	static String param_LiberalCode;
	
	public static void main(String[] args) {

		/* ��Ÿ���� ���� �ʱ�ȭ */
		param_year = "2019";
		param_session = "1";
		param_orgSect = "A";
		param_camSect = "H2";
		param_gubun = "1";
		param_MajorCode = "AQR02_H2";
		param_LiberalCode = "303_H2";
		
		// ��� ���� �� ���� ���� �ڵ带 ������ HashMap ArrayList ����
		majorCodeList = new ArrayList<HashMap<String, String>>();
		liberalCodeList = new ArrayList<HashMap<String, String>>();
		// ��� ���� �� ���� ���� ������ ������ HashMap ArrayList ����
		majorLecList = new ArrayList<HashMap<String, String>>();
		liberalLecList = new ArrayList<HashMap<String, String>>();
		
//		/* Just for single parse test */
//		Document lecInitDoc = getPageDoc("https://wis.hufs.ac.kr/src08/jsp/lecture/LECTURE2020L.jsp?tab_lang=K&ag_ledg_year=2019&ag_ledg_sessn=1&ag_org_sect=A&campus_sect=H2");
//		
//		/* 1. ���� ���� �ڵ� �Ľ� (�۷ι� ����) */
//		Elements majorCodeElements = lecInitDoc.select("select[name=ag_crs_strct_cd] option");
//		
//		for(int i = 0; i < majorCodeElements.size(); i++) {
//			HashMap<String, String> temp = new HashMap<String, String>();
//			temp.put("code", majorCodeElements.get(i).attr("value"));
//			temp.put("title", majorCodeElements.get(i).text().substring(8));
//			//System.out.println(temp.get("code") + "\t" + temp.get("title"));
//			majorCodeList.add(temp);
//		}
//		
//		/* 2. ���� ���� ���� object �Ľ� (�۷ι� ����) */
//		// test : ��� ���� ���� code�� ������ ���� �������� GET�ϰ� �� ���� object�� �Ľ��Ͽ� majorLectList�� ���� �Ѵ�.
//		System.out.print("Parsing major lectures now...");
//		for(int i = 0; i < majorCodeList.size(); i++) {
//			Document majorLecDoc = getMajorLecDoc(param_year, param_session, param_orgSect, param_camSect, param_MajorCode);
//			Elements majorLecElements = majorLecDoc.select("div[id=premier1] tbody tr");
//			for(int j = 1; j < majorLecElements.size(); j++) {
//				// ���� ����
//				Elements majorLecTdElements = majorLecElements.get(1).select("td");
//				//temp.put("raw", majorLecElements.get(j).text());
//				/*
//				temp.put("area", majorLecTdElements.get(1).text());
//				temp.put("year", majorLecTdElements.get(2).text());
//				temp.put("code", majorLecTdElements.get(3).text());
//				temp.put("title", majorLecTdElements.get(4).text());
//				temp.put("prof", majorLecTdElements.get(11).text());
//				temp.put("credit", majorLecTdElements.get(12).text());
//				temp.put("time", majorLecTdElements.get(13).text());
//				temp.put("sched", majorLecTdElements.get(14).text());
//				temp.put("numpeople", majorLecTdElements.get(15).text());
//				temp.put("note", majorLecTdElements.get(16).text());
//				*/
//				//System.out.println(temp.get("raw")); // ��� test
//				majorLecList.add(temp);
//			}
//			System.out.print(".");
//		}
//		System.out.println("Done!");
//		System.out.println("majorLecList size (��� ���� ���� ��) : " + majorLecList.size());
		
//		param_gubun = "2";
//		param_LiberalCode = "334_H2";
//		Document liberalLecDoc = getLiberalLecDoc(param_year, param_session, param_orgSect, param_camSect, param_LiberalCode);
//		Elements majorLecElements = liberalLecDoc.select("div[id=premier1] tbody tr");
//		System.out.println(majorLecElements.get(1).select("td").get(7).select("img").isEmpty());
//		System.out.println(majorLecElements.get(5).select("td").get(7).select("img").isEmpty());
		/* Start Parsing task */
		startMajorLecParsing();
		startLiberalLecParsing();
		
		/* Excel file out �ך� */
		saveParseResultAsXls(true);
		saveParseResultAsXls(false);
		
		/* Text file out �׽�Ʈ */
//		saveMajorParseResultAsTxt();
//		saveMajorParseResultAsTxt2();
//		saveLiberalParseResultAsTxt();

	}

	private static void saveMajorParseResultAsTxt2() {
		majorLecList.clear();
		try {
			FileWriter fw = new FileWriter("./src/major_titles.txt"); // �����ּ� ��� ����
			BufferedWriter bw = new BufferedWriter(fw);

			System.out.print("Saving the parsing result as a txt file...");
			for (int i = 0; i < majorCodeList.size(); i++) {
				
				bw.write(majorCodeList.get(i).get("title").substring(0, majorCodeList.get(i).get("title").indexOf('(')-1));
				bw.newLine(); // �ٹٲ�

				System.out.print(".");
				bw.newLine();
			}
			System.out.println("Done!");
			bw.close();
		} catch (IOException e) {
			System.err.println(e); // ������ �ִٸ� �޽��� ���
			System.exit(1);
		}
	}

	private static void startLiberalLecParsing() {
		Document lecInitDoc = getPageDoc("https://wis.hufs.ac.kr/src08/jsp/lecture/LECTURE2020L.jsp?tab_lang=K&ag_ledg_year=2019&ag_ledg_sessn=1&ag_org_sect=A&campus_sect=H2");
		
		/* 1. ���� ���� �ڵ� �Ľ� (�۷ι� ����) */	
		Elements liberalCodeElements = lecInitDoc.select("select[name=ag_compt_fld_cd] option");
		
		for(int i = 0; i < liberalCodeElements.size(); i++) {
			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("code", liberalCodeElements.get(i).attr("value"));
			temp.put("title", liberalCodeElements.get(i).text());
			//System.out.println(temp.get("code") + "\t" + temp.get("title"));
			liberalCodeList.add(temp);
		}
		
		/* 2. ���� ���� ���� object �Ľ� (�۷ι� ����) */
		// test : ��� ���� ���� code�� ������ ���� �������� GET�ϰ� �� ���� object�� �Ľ��Ͽ� liberalLectList�� ���� �Ѵ�.
		System.out.print("Parsing liberal lectures now...");
		for(int i = 0; i < liberalCodeList.size(); i++) {
			Document liberalLecDoc = getLiberalLecDoc(param_year, param_session, param_orgSect, param_camSect, 
					liberalCodeList.get(i).get("code"));
			Elements liberalLecElements = liberalLecDoc.select("div[id=premier1] tbody tr");
			for(int j = 1; j < liberalLecElements.size(); j++) {
				// ���� ����
				Elements liberalLecTdElements = liberalLecElements.get(j).select("td");
				HashMap<String, String> temp = new HashMap<String, String>();
				//temp.put("raw", liberalLecElements.get(j).text());
				temp.put("gubun", liberalCodeList.get(i).get("title"));
				temp.put("area", liberalLecTdElements.get(1).text());
				temp.put("year", liberalLecTdElements.get(2).text());
				temp.put("code", liberalLecTdElements.get(3).text());
				temp.put("title", liberalLecTdElements.get(4).text());
				if(liberalLecTdElements.get(6).select("img").isEmpty()) {
					temp.put("junpil", "0");
				} else {
					temp.put("junpil", "1"); // ������ ���
				}
				
				if(liberalLecTdElements.get(7).select("img").isEmpty()) {
					temp.put("cyber", "0");
				} else {
					temp.put("cyber", "1"); // �¶��ΰ����� ���
				}
				
				if(liberalLecTdElements.get(8).select("img").isEmpty()) {
					temp.put("muke", "0");
				} else {
					temp.put("muke", "1"); // ��ũ�� ���
				}
				
				if(liberalLecTdElements.get(9).select("img").isEmpty()) {
					temp.put("foreign", "0");
				} else {
					temp.put("foreign", "1"); // ������ ���
				}
				
				if(liberalLecTdElements.get(10).select("img").isEmpty()) {
					temp.put("team", "0");
				} else {
					temp.put("team", "1"); // ��ƼĪ�� ���
				}
				temp.put("prof", liberalLecTdElements.get(11).text());
				temp.put("credit", liberalLecTdElements.get(12).text());
				temp.put("time", liberalLecTdElements.get(13).text());
				temp.put("sched", liberalLecTdElements.get(14).text());
				temp.put("numpeople", liberalLecTdElements.get(15).text());
				temp.put("note", liberalLecTdElements.get(16).text());
				//System.out.println(temp.get("raw")); // ��� test
				liberalLecList.add(temp);
			}
			System.out.print(".");
		}
		System.out.println("Done!");
		System.out.println("liberalLecList size (��� ���� ���� ��) : " + liberalLecList.size());
	}

	private static void startMajorLecParsing() {
		Document lecInitDoc = getPageDoc("https://wis.hufs.ac.kr/src08/jsp/lecture/LECTURE2020L.jsp?tab_lang=K&ag_ledg_year=2019&ag_ledg_sessn=1&ag_org_sect=A&campus_sect=H2");
		
		/* 1. ���� ���� �ڵ� �Ľ� (�۷ι� ����) */
		Elements majorCodeElements = lecInitDoc.select("select[name=ag_crs_strct_cd] option");
		
		for(int i = 0; i < majorCodeElements.size(); i++) {
			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("code", majorCodeElements.get(i).attr("value"));
			temp.put("title", majorCodeElements.get(i).text().substring(8));
			//System.out.println(temp.get("code") + "\t" + temp.get("title"));
			majorCodeList.add(temp);
		}
		
		/* 2. ���� ���� ���� object �Ľ� (�۷ι� ����) */
		// test : ��� ���� ���� code�� ������ ���� �������� GET�ϰ� �� ���� object�� �Ľ��Ͽ� majorLectList�� ���� �Ѵ�.
		System.out.print("Parsing major lectures now...");
		for(int i = 0; i < majorCodeList.size(); i++) {
			Document majorLecDoc = getMajorLecDoc(param_year, param_session, param_orgSect, param_camSect, 
					majorCodeList.get(i).get("code"));
			Elements majorLecElements = majorLecDoc.select("div[id=premier1] tbody tr");
			for(int j = 1; j < majorLecElements.size(); j++) {
				// ���� ����
				Elements majorLecTdElements = majorLecElements.get(j).select("td");
				HashMap<String, String> temp = new HashMap<String, String>();
				//temp.put("raw", majorLecElements.get(j).text());
				temp.put("gubun", majorCodeList.get(i).get("title"));
				temp.put("area", majorLecTdElements.get(1).text());
				temp.put("year", majorLecTdElements.get(2).text());
				temp.put("code", majorLecTdElements.get(3).text());
				temp.put("title", majorLecTdElements.get(4).text());
				if(majorLecTdElements.get(6).select("img").isEmpty()) {
					temp.put("junpil", "0");
				} else {
					temp.put("junpil", "1"); // ������ ���
				}
				
				if(majorLecTdElements.get(7).select("img").isEmpty()) {
					temp.put("cyber", "0");
				} else {
					temp.put("cyber", "1"); // �¶��ΰ����� ���
				}
				
				if(majorLecTdElements.get(8).select("img").isEmpty()) {
					temp.put("muke", "0");
				} else {
					temp.put("muke", "1"); // ��ũ�� ���
				}
				
				if(majorLecTdElements.get(9).select("img").isEmpty()) {
					temp.put("foreign", "0");
				} else {
					temp.put("foreign", "1"); // ������ ���
				}
				
				if(majorLecTdElements.get(10).select("img").isEmpty()) {
					temp.put("team", "0");
				} else {
					temp.put("team", "1"); // ��ƼĪ�� ���
				}
				temp.put("prof", majorLecTdElements.get(11).text());
				temp.put("credit", majorLecTdElements.get(12).text());
				temp.put("time", majorLecTdElements.get(13).text());
				temp.put("sched", majorLecTdElements.get(14).text());
				temp.put("numpeople", majorLecTdElements.get(15).text());
				temp.put("note", majorLecTdElements.get(16).text());
				//System.out.println(temp.get("raw")); // ��� test
				majorLecList.add(temp);
			}
			System.out.print(".");
		}
		System.out.println("Done!");
		System.out.println("majorLecList size (��� ���� ���� ��) : " + majorLecList.size());
	}

	private static void saveParseResultAsXls(boolean isMajor) {
		String path = null;
		String doneMessage = null;
		List<HashMap<String, String>> tempLecList = null;
		if(isMajor) {
			path = "./src/majorParseResult.xls";
			doneMessage = "All major lecture parsing result has been saved as a xls file!";
			tempLecList = majorLecList;
			
		} else {
			path = "./src/liberalParseResult.xls";
			doneMessage = "All liberal lecture parsing result has been saved as a xls file!";
			tempLecList = liberalLecList;
		}
		
        // ��ũ�� ����
        HSSFWorkbook workbook = new HSSFWorkbook();
        // ��ũ��Ʈ ����
        HSSFSheet sheet = workbook.createSheet();
        // �� ����
        HSSFRow row = sheet.createRow(0);
        // �� ����
        HSSFCell cell;
        
        // ��� ���� ����
        cell = row.createCell(0);
        cell.setCellValue("No.");
        
        cell = row.createCell(1);
        cell.setCellValue("����");
        
        cell = row.createCell(2);
        cell.setCellValue("��������");
        
        cell = row.createCell(3);
        cell.setCellValue("�г�");
        
        cell = row.createCell(4);
        cell.setCellValue("�м���ȣ");
        
        cell = row.createCell(5);
        cell.setCellValue("�������");
        
        cell = row.createCell(6);
        cell.setCellValue("��米��");
        
        cell = row.createCell(7);
        cell.setCellValue("����");
        
        cell = row.createCell(8);
        cell.setCellValue("�ð�");
        
        cell = row.createCell(9);
        cell.setCellValue("���ǽð�/���ǽ�");
        
        cell = row.createCell(10);
        cell.setCellValue("��û/�����ο�");
        
        cell = row.createCell(11);
        cell.setCellValue("���");
        
        cell = row.createCell(12);
        cell.setCellValue("����");
        
        cell = row.createCell(13);
        cell.setCellValue("�¶���");
        
        cell = row.createCell(14);
        cell.setCellValue("��ũ");
        
        cell = row.createCell(15);
        cell.setCellValue("����");
        
        cell = row.createCell(16);
        cell.setCellValue("��ƼĪ");
        
        // ����Ʈ�� size ��ŭ row�� ����
        for(int rowIdx=0; rowIdx < tempLecList.size(); rowIdx++) {
        	
            HashMap<String, String> temp = tempLecList.get(rowIdx);
            // �� ����
            row = sheet.createRow(rowIdx+1);
            
            cell = row.createCell(0);
            cell.setCellValue(rowIdx+1);
            
            cell = row.createCell(1);
            cell.setCellValue(temp.get("gubun"));
            
            cell = row.createCell(2);
            cell.setCellValue(temp.get("area"));
            
            cell = row.createCell(3);
            cell.setCellValue(temp.get("year"));
            
            cell = row.createCell(4);
            cell.setCellValue(temp.get("code"));
            
            cell = row.createCell(5);
            cell.setCellValue(temp.get("title"));
            
            cell = row.createCell(6);
            cell.setCellValue(temp.get("prof"));
            
            cell = row.createCell(7);
            cell.setCellValue(temp.get("credit"));
            
            cell = row.createCell(8);
            cell.setCellValue(temp.get("time"));
            
            cell = row.createCell(9);
            cell.setCellValue(temp.get("sched"));
            
            cell = row.createCell(10);
            cell.setCellValue(temp.get("numpeople"));
            
            cell = row.createCell(11);
            cell.setCellValue(temp.get("note"));
            
            cell = row.createCell(12);
            cell.setCellValue(temp.get("junpil"));
            
            cell = row.createCell(13);
            cell.setCellValue(temp.get("cyber"));
            
            cell = row.createCell(14);
            cell.setCellValue(temp.get("muke"));
            
            cell = row.createCell(15);
            cell.setCellValue(temp.get("foreign"));
            
            cell = row.createCell(16);
            cell.setCellValue(temp.get("team"));
            
        }
		
        // �Էµ� ���� ���Ϸ� ����
        File file = new File(path);
        FileOutputStream fos = null;
        
        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(workbook!=null) workbook.close();
                if(fos!=null) fos.close();
                System.out.println(doneMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	private static void saveLiberalParseResultAsTxt() {
		liberalLecList.clear();
		try {
			FileWriter fw = new FileWriter("./src/liberal_result.txt"); // �����ּ� ��� ����
			BufferedWriter bw = new BufferedWriter(fw);

			System.out.print("Saving the parsing result as a txt file...");
			for (int i = 0; i < liberalCodeList.size(); i++) {
				
				bw.write("========== " + liberalCodeList.get(i).get("title") + " ==========");
				bw.newLine(); // �ٹٲ�

				Document liberalLecDoc = getLiberalLecDoc(param_year, param_session, param_orgSect, param_camSect,
						liberalCodeList.get(i).get("code"));
				Elements liberalLecElements = liberalLecDoc.select("div[id=premier1] tbody tr");
				for (int j = 1; j < liberalLecElements.size(); j++) {
					// �ϴ��� ��°�� String ���·� ����, ���� ������ ���Ŀ�
					HashMap<String, String> temp = new HashMap<String, String>();
					temp.put("raw", liberalLecElements.get(j).text());
					bw.write(temp.get("raw"));
					bw.newLine(); // �ٹٲ�
					liberalLecList.add(temp);
				}
				System.out.print(".");
				bw.newLine();
			}
			
			System.out.println("Done!");

			System.out.println(liberalLecList.size() + " liberal lectures has been parsed successfully.");
			bw.write(liberalLecList.size() + " liberal lectures has been parsed successfully.");
			bw.newLine(); // �ٹٲ�

			bw.close();
		} catch (IOException e) {
			System.err.println(e); // ������ �ִٸ� �޽��� ���
			System.exit(1);
		}
	}

	private static void saveMajorParseResultAsTxt() {
		majorLecList.clear();
		try {
			FileWriter fw = new FileWriter("./src/major_result.txt"); // �����ּ� ��� ����
			BufferedWriter bw = new BufferedWriter(fw);

			System.out.print("Saving the parsing result as a txt file...");
			for (int i = 0; i < majorCodeList.size(); i++) {
				
				bw.write("========== " + majorCodeList.get(i).get("title") + " ==========");
				bw.newLine(); // �ٹٲ�

				Document majorLecDoc = getMajorLecDoc(param_year, param_session, param_orgSect, param_camSect,
						majorCodeList.get(i).get("code"));
				Elements majorLecElements = majorLecDoc.select("div[id=premier1] tbody tr");
				for (int j = 1; j < majorLecElements.size(); j++) {
					// �ϴ��� ��°�� String ���·� ����, ���� ������ ���Ŀ�
					HashMap<String, String> temp = new HashMap<String, String>();
					temp.put("raw", majorLecElements.get(j).text());
					bw.write(temp.get("raw"));
					bw.newLine(); // �ٹٲ�
					majorLecList.add(temp);
				}
				System.out.print(".");
				bw.newLine();
			}
			
			System.out.println("Done!");

			System.out.println(majorLecList.size() + " major lectures has been parsed successfully.");
			bw.write(majorLecList.size() + " major lectures has been parsed successfully.");
			bw.newLine(); // �ٹٲ�

			bw.close();
		} catch (IOException e) {
			System.err.println(e); // ������ �ִٸ� �޽��� ���
			System.exit(1);
		}
		
	}

	private static Document getPageDoc(String url) {
		Document sampleDoc = null;
		try {
			sampleDoc = Jsoup.connect(url)
					.userAgent(userAgent)
					.method(Connection.Method.GET)
					.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sampleDoc;
	}
	
	private static Document getMajorLecDoc(String year, String session, String orgSect, String camSect, String majorCode) {
		Document sampleDoc = null;
		// �������� ������ GET
		String majorLecURL = "https://wis.hufs.ac.kr/src08/jsp/lecture/LECTURE2020L.jsp" + 
				"?tab_lang=K" +
				"&ag_ledg_year=" + year + 
				"&ag_ledg_sessn=" + session +
				"&ag_org_sect=" + orgSect +
				"&campus_sect=" + camSect +
				"&gubun=1" +
				"&ag_crs_strct_cd=" + majorCode;
		//System.out.println(majorLecURL);
		try {
			sampleDoc = Jsoup.connect(majorLecURL)
					.userAgent(userAgent)
					.method(Connection.Method.GET)
					.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sampleDoc;
	}
	
	private static Document getLiberalLecDoc(String year, String session, String orgSect, String camSect, String liberalCode) {
		Document sampleDoc = null;
		// �������� ������ GET
		String liberalLecURL = "https://wis.hufs.ac.kr/src08/jsp/lecture/LECTURE2020L.jsp" + 
				"?tab_lang=K" +
				"&ag_ledg_year=" + year + 
				"&ag_ledg_sessn=" + session +
				"&ag_org_sect=" + orgSect +
				"&campus_sect=" + camSect +
				"&gubun=2" +
				"&ag_compt_fld_cd=" + liberalCode;
		try {
			sampleDoc = Jsoup.connect(liberalLecURL)
					.userAgent(userAgent)
					.method(Connection.Method.GET)
					.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sampleDoc;
	}
}
