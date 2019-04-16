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
	ag_ledg_year(연도) : 2019
	ag_ledg_sessn(학기) : 1(1학기), 2(여름계절), 3(2학기), 4(겨울계절)
	ag_org_sect(소속구분) : A(학부), B(대학원) ... 
	campus_sect(캠퍼스 구분) : H1(서울), H2(글로벌)
	gubun(전공/교양 선택 구분) : 1(전공영역을 선택한 경우), 2(교양영역을 선택한 경우)
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

		/* 파타메터 변수 초기화 */
		param_year = "2019";
		param_session = "1";
		param_orgSect = "A";
		param_camSect = "H2";
		param_gubun = "1";
		param_MajorCode = "AQR02_H2";
		param_LiberalCode = "303_H2";
		
		// 모든 전공 및 교양 영역 코드를 저장할 HashMap ArrayList 생성
		majorCodeList = new ArrayList<HashMap<String, String>>();
		liberalCodeList = new ArrayList<HashMap<String, String>>();
		// 모든 전공 및 교양 강의 과목을 저장할 HashMap ArrayList 생성
		majorLecList = new ArrayList<HashMap<String, String>>();
		liberalLecList = new ArrayList<HashMap<String, String>>();
		
//		/* Just for single parse test */
//		Document lecInitDoc = getPageDoc("https://wis.hufs.ac.kr/src08/jsp/lecture/LECTURE2020L.jsp?tab_lang=K&ag_ledg_year=2019&ag_ledg_sessn=1&ag_org_sect=A&campus_sect=H2");
//		
//		/* 1. 전공 영역 코드 파싱 (글로벌 기준) */
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
//		/* 2. 전공 영역 강의 object 파싱 (글로벌 기준) */
//		// test : 모든 전공 영역 code를 대입한 강의 페이지를 GET하고 각 강의 object를 파싱하여 majorLectList에 저장 한다.
//		System.out.print("Parsing major lectures now...");
//		for(int i = 0; i < majorCodeList.size(); i++) {
//			Document majorLecDoc = getMajorLecDoc(param_year, param_session, param_orgSect, param_camSect, param_MajorCode);
//			Elements majorLecElements = majorLecDoc.select("div[id=premier1] tbody tr");
//			for(int j = 1; j < majorLecElements.size(); j++) {
//				// 세부 구분
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
//				//System.out.println(temp.get("raw")); // 출력 test
//				majorLecList.add(temp);
//			}
//			System.out.print(".");
//		}
//		System.out.println("Done!");
//		System.out.println("majorLecList size (모든 전공 강의 수) : " + majorLecList.size());
		
//		param_gubun = "2";
//		param_LiberalCode = "334_H2";
//		Document liberalLecDoc = getLiberalLecDoc(param_year, param_session, param_orgSect, param_camSect, param_LiberalCode);
//		Elements majorLecElements = liberalLecDoc.select("div[id=premier1] tbody tr");
//		System.out.println(majorLecElements.get(1).select("td").get(7).select("img").isEmpty());
//		System.out.println(majorLecElements.get(5).select("td").get(7).select("img").isEmpty());
		/* Start Parsing task */
		startMajorLecParsing();
		startLiberalLecParsing();
		
		/* Excel file out 테슽 */
		saveParseResultAsXls(true);
		saveParseResultAsXls(false);
		
		/* Text file out 테스트 */
//		saveMajorParseResultAsTxt();
//		saveMajorParseResultAsTxt2();
//		saveLiberalParseResultAsTxt();

	}

	private static void saveMajorParseResultAsTxt2() {
		majorLecList.clear();
		try {
			FileWriter fw = new FileWriter("./src/major_titles.txt"); // 절대주소 경로 가능
			BufferedWriter bw = new BufferedWriter(fw);

			System.out.print("Saving the parsing result as a txt file...");
			for (int i = 0; i < majorCodeList.size(); i++) {
				
				bw.write(majorCodeList.get(i).get("title").substring(0, majorCodeList.get(i).get("title").indexOf('(')-1));
				bw.newLine(); // 줄바꿈

				System.out.print(".");
				bw.newLine();
			}
			System.out.println("Done!");
			bw.close();
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
			System.exit(1);
		}
	}

	private static void startLiberalLecParsing() {
		Document lecInitDoc = getPageDoc("https://wis.hufs.ac.kr/src08/jsp/lecture/LECTURE2020L.jsp?tab_lang=K&ag_ledg_year=2019&ag_ledg_sessn=1&ag_org_sect=A&campus_sect=H2");
		
		/* 1. 교양 영역 코드 파싱 (글로벌 기준) */	
		Elements liberalCodeElements = lecInitDoc.select("select[name=ag_compt_fld_cd] option");
		
		for(int i = 0; i < liberalCodeElements.size(); i++) {
			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("code", liberalCodeElements.get(i).attr("value"));
			temp.put("title", liberalCodeElements.get(i).text());
			//System.out.println(temp.get("code") + "\t" + temp.get("title"));
			liberalCodeList.add(temp);
		}
		
		/* 2. 교양 영역 강의 object 파싱 (글로벌 기준) */
		// test : 모든 교양 영역 code를 대입한 강의 페이지를 GET하고 각 강의 object를 파싱하여 liberalLectList에 저장 한다.
		System.out.print("Parsing liberal lectures now...");
		for(int i = 0; i < liberalCodeList.size(); i++) {
			Document liberalLecDoc = getLiberalLecDoc(param_year, param_session, param_orgSect, param_camSect, 
					liberalCodeList.get(i).get("code"));
			Elements liberalLecElements = liberalLecDoc.select("div[id=premier1] tbody tr");
			for(int j = 1; j < liberalLecElements.size(); j++) {
				// 세부 구분
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
					temp.put("junpil", "1"); // 전필인 경우
				}
				
				if(liberalLecTdElements.get(7).select("img").isEmpty()) {
					temp.put("cyber", "0");
				} else {
					temp.put("cyber", "1"); // 온라인강의인 경우
				}
				
				if(liberalLecTdElements.get(8).select("img").isEmpty()) {
					temp.put("muke", "0");
				} else {
					temp.put("muke", "1"); // 무크인 경우
				}
				
				if(liberalLecTdElements.get(9).select("img").isEmpty()) {
					temp.put("foreign", "0");
				} else {
					temp.put("foreign", "1"); // 원어인 경우
				}
				
				if(liberalLecTdElements.get(10).select("img").isEmpty()) {
					temp.put("team", "0");
				} else {
					temp.put("team", "1"); // 팀티칭인 경우
				}
				temp.put("prof", liberalLecTdElements.get(11).text());
				temp.put("credit", liberalLecTdElements.get(12).text());
				temp.put("time", liberalLecTdElements.get(13).text());
				temp.put("sched", liberalLecTdElements.get(14).text());
				temp.put("numpeople", liberalLecTdElements.get(15).text());
				temp.put("note", liberalLecTdElements.get(16).text());
				//System.out.println(temp.get("raw")); // 출력 test
				liberalLecList.add(temp);
			}
			System.out.print(".");
		}
		System.out.println("Done!");
		System.out.println("liberalLecList size (모든 교양 강의 수) : " + liberalLecList.size());
	}

	private static void startMajorLecParsing() {
		Document lecInitDoc = getPageDoc("https://wis.hufs.ac.kr/src08/jsp/lecture/LECTURE2020L.jsp?tab_lang=K&ag_ledg_year=2019&ag_ledg_sessn=1&ag_org_sect=A&campus_sect=H2");
		
		/* 1. 전공 영역 코드 파싱 (글로벌 기준) */
		Elements majorCodeElements = lecInitDoc.select("select[name=ag_crs_strct_cd] option");
		
		for(int i = 0; i < majorCodeElements.size(); i++) {
			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("code", majorCodeElements.get(i).attr("value"));
			temp.put("title", majorCodeElements.get(i).text().substring(8));
			//System.out.println(temp.get("code") + "\t" + temp.get("title"));
			majorCodeList.add(temp);
		}
		
		/* 2. 전공 영역 강의 object 파싱 (글로벌 기준) */
		// test : 모든 전공 영역 code를 대입한 강의 페이지를 GET하고 각 강의 object를 파싱하여 majorLectList에 저장 한다.
		System.out.print("Parsing major lectures now...");
		for(int i = 0; i < majorCodeList.size(); i++) {
			Document majorLecDoc = getMajorLecDoc(param_year, param_session, param_orgSect, param_camSect, 
					majorCodeList.get(i).get("code"));
			Elements majorLecElements = majorLecDoc.select("div[id=premier1] tbody tr");
			for(int j = 1; j < majorLecElements.size(); j++) {
				// 세부 구분
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
					temp.put("junpil", "1"); // 전필인 경우
				}
				
				if(majorLecTdElements.get(7).select("img").isEmpty()) {
					temp.put("cyber", "0");
				} else {
					temp.put("cyber", "1"); // 온라인강의인 경우
				}
				
				if(majorLecTdElements.get(8).select("img").isEmpty()) {
					temp.put("muke", "0");
				} else {
					temp.put("muke", "1"); // 무크인 경우
				}
				
				if(majorLecTdElements.get(9).select("img").isEmpty()) {
					temp.put("foreign", "0");
				} else {
					temp.put("foreign", "1"); // 원어인 경우
				}
				
				if(majorLecTdElements.get(10).select("img").isEmpty()) {
					temp.put("team", "0");
				} else {
					temp.put("team", "1"); // 팀티칭인 경우
				}
				temp.put("prof", majorLecTdElements.get(11).text());
				temp.put("credit", majorLecTdElements.get(12).text());
				temp.put("time", majorLecTdElements.get(13).text());
				temp.put("sched", majorLecTdElements.get(14).text());
				temp.put("numpeople", majorLecTdElements.get(15).text());
				temp.put("note", majorLecTdElements.get(16).text());
				//System.out.println(temp.get("raw")); // 출력 test
				majorLecList.add(temp);
			}
			System.out.print(".");
		}
		System.out.println("Done!");
		System.out.println("majorLecList size (모든 전공 강의 수) : " + majorLecList.size());
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
		
        // 워크북 생성
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 워크시트 생성
        HSSFSheet sheet = workbook.createSheet();
        // 행 생성
        HSSFRow row = sheet.createRow(0);
        // 쎌 생성
        HSSFCell cell;
        
        // 헤더 정보 구성
        cell = row.createCell(0);
        cell.setCellValue("No.");
        
        cell = row.createCell(1);
        cell.setCellValue("구분");
        
        cell = row.createCell(2);
        cell.setCellValue("개설영역");
        
        cell = row.createCell(3);
        cell.setCellValue("학년");
        
        cell = row.createCell(4);
        cell.setCellValue("학수번호");
        
        cell = row.createCell(5);
        cell.setCellValue("교과목명");
        
        cell = row.createCell(6);
        cell.setCellValue("담당교수");
        
        cell = row.createCell(7);
        cell.setCellValue("학점");
        
        cell = row.createCell(8);
        cell.setCellValue("시간");
        
        cell = row.createCell(9);
        cell.setCellValue("강의시간/강의실");
        
        cell = row.createCell(10);
        cell.setCellValue("신청/제한인원");
        
        cell = row.createCell(11);
        cell.setCellValue("비고");
        
        cell = row.createCell(12);
        cell.setCellValue("전필");
        
        cell = row.createCell(13);
        cell.setCellValue("온라인");
        
        cell = row.createCell(14);
        cell.setCellValue("무크");
        
        cell = row.createCell(15);
        cell.setCellValue("원어");
        
        cell = row.createCell(16);
        cell.setCellValue("팀티칭");
        
        // 리스트의 size 만큼 row를 생성
        for(int rowIdx=0; rowIdx < tempLecList.size(); rowIdx++) {
        	
            HashMap<String, String> temp = tempLecList.get(rowIdx);
            // 행 생성
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
		
        // 입력된 내용 파일로 쓰기
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
			FileWriter fw = new FileWriter("./src/liberal_result.txt"); // 절대주소 경로 가능
			BufferedWriter bw = new BufferedWriter(fw);

			System.out.print("Saving the parsing result as a txt file...");
			for (int i = 0; i < liberalCodeList.size(); i++) {
				
				bw.write("========== " + liberalCodeList.get(i).get("title") + " ==========");
				bw.newLine(); // 줄바꿈

				Document liberalLecDoc = getLiberalLecDoc(param_year, param_session, param_orgSect, param_camSect,
						liberalCodeList.get(i).get("code"));
				Elements liberalLecElements = liberalLecDoc.select("div[id=premier1] tbody tr");
				for (int j = 1; j < liberalLecElements.size(); j++) {
					// 일단은 통째로 String 형태로 저장, 세부 구분은 추후에
					HashMap<String, String> temp = new HashMap<String, String>();
					temp.put("raw", liberalLecElements.get(j).text());
					bw.write(temp.get("raw"));
					bw.newLine(); // 줄바꿈
					liberalLecList.add(temp);
				}
				System.out.print(".");
				bw.newLine();
			}
			
			System.out.println("Done!");

			System.out.println(liberalLecList.size() + " liberal lectures has been parsed successfully.");
			bw.write(liberalLecList.size() + " liberal lectures has been parsed successfully.");
			bw.newLine(); // 줄바꿈

			bw.close();
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
			System.exit(1);
		}
	}

	private static void saveMajorParseResultAsTxt() {
		majorLecList.clear();
		try {
			FileWriter fw = new FileWriter("./src/major_result.txt"); // 절대주소 경로 가능
			BufferedWriter bw = new BufferedWriter(fw);

			System.out.print("Saving the parsing result as a txt file...");
			for (int i = 0; i < majorCodeList.size(); i++) {
				
				bw.write("========== " + majorCodeList.get(i).get("title") + " ==========");
				bw.newLine(); // 줄바꿈

				Document majorLecDoc = getMajorLecDoc(param_year, param_session, param_orgSect, param_camSect,
						majorCodeList.get(i).get("code"));
				Elements majorLecElements = majorLecDoc.select("div[id=premier1] tbody tr");
				for (int j = 1; j < majorLecElements.size(); j++) {
					// 일단은 통째로 String 형태로 저장, 세부 구분은 추후에
					HashMap<String, String> temp = new HashMap<String, String>();
					temp.put("raw", majorLecElements.get(j).text());
					bw.write(temp.get("raw"));
					bw.newLine(); // 줄바꿈
					majorLecList.add(temp);
				}
				System.out.print(".");
				bw.newLine();
			}
			
			System.out.println("Done!");

			System.out.println(majorLecList.size() + " major lectures has been parsed successfully.");
			bw.write(majorLecList.size() + " major lectures has been parsed successfully.");
			bw.newLine(); // 줄바꿈

			bw.close();
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
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
		// 전공영역 페이지 GET
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
		// 전공영역 페이지 GET
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
