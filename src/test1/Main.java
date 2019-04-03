package test1;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
	
	static Map<String, String> loginCookie = null;
	// Windows, Whale의 User Agent.
	static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Whale/1.4.64.6 Safari/537.36";
	
	public static void main(String[] args) throws IOException, Exception {
		
		// Set header values
		Map<String, String> defaultHeader = new HashMap<String, String>();
		defaultHeader.put("Referer", "");
		
		// parameter 값으로 form data 전송
		String sID = "201402783";
		String sha512PW = "8b8b6aa4bf808f01017bc1fb50960a18b3861f6ae269b138de8ff975c15b4ab607a2c3847301bb74d1b6ac106d15edbb9d7baf57826bbecbb0b5cc9d9c5948c3";
		String loginURL = "https://eclass2.hufs.ac.kr:4443/ilos/lo/login.acl?usr_id=" + sID +
							"&usr_pwd=" + sha512PW;
		// 로그인(POST) - HTTPS
    	Connection.Response response1 = Jsoup.connect(loginURL)
				.userAgent(userAgent)
				.timeout(3000)
				.header("Origin", "http://eclass2.hufs.ac.kr:8181")
				.header("Referer", "http://eclass2.hufs.ac.kr:8181/ilos/main/member/login_form.acl")
				.header("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
				.header("Content-Type", "application/x-www-form-urlencoded")
				.header("Connection", "keep-alive")
				.header("Accept-Encoding", "gzip, deflate")
				.header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
				.method(Connection.Method.POST)
				.execute();
		//System.out.println(response1.statusMessage());
		// 로그인 성공 후 얻은 쿠키.
		// 쿠키 중 TSESSION이라는 값을 확인할 수 있다.
		loginCookie = response1.cookies();
		
		if(loginCookie.containsKey("JSESSIONID")) {
			System.out.println("JSESSIONID FOUND!");
		}
		else {
			System.out.println("JSESSIONID NOT FOUND!!");
		}
		
		// 이클래스 메인 페이지 GET
		Document mainPage = getPageDocument("http://eclass2.hufs.ac.kr:8181/ilos/main/main_form.acl");
		//System.out.println(mainPage.toString());
		
		// 수강과목만 파싱
		Elements e1 = mainPage.select("em[class=sub_open]");
		List<String> lectureList = new ArrayList<String>(); // 수강과목 이름 리스트
		List<String> lectureCodeList = new ArrayList<String>(); // 수강과목 코드 과 URL 리스트
		
		for(Element lec : e1) {
			String lecRawTitle = lec.attr("title");
			String lecRawCode = lec.attr("onclick");
			lectureList.add(lecRawTitle.substring(0, lecRawTitle.length() - 9));
			lectureCodeList.add(lecRawCode.substring(lecRawCode.indexOf('\'') + 1, lecRawCode.lastIndexOf('\'')));
			//System.out.println(lecRawCode.substring(lecRawCode.indexOf('\'') + 1, lecRawCode.lastIndexOf('\'')));
			//System.out.println(lecRawTitle.substring(0, lecRawTitle.length() - 9));
		}
		//System.out.println(lectureList.size());
		//System.out.println(lectureCodeList.size());
		
		for(int i = 0; i < lectureList.size(); i++) {

			// Refresh connection on every lecture
			eclassRoomConnect(lectureCodeList.get(i));
			
	    	// 강의실 공지사항 게시판 페이지  GET
			Document eclassNoticePage = getPageDocument("http://eclass2.hufs.ac.kr:8181/ilos/st/course/notice_list_form.acl");
			
			// 공지사항 조지기
			System.out.println("*** [" + lectureList.get(i) + "] 공지사항 최근 게시물 ***");
			if(!eclassNoticePage.select("table[class=bbslist] tbody tr td[class=left]").isEmpty()) {
				Elements noticeTitles = eclassNoticePage.select("table[class=bbslist] tbody tr");
				// 가장 최근 공지사항 게시물의 제목과 게시 일자 출력
				System.out.println("===> " + noticeTitles.get(0).select("td").get(1).text() + "\t" + 
									noticeTitles.get(0).select("td").get(4).text());	
			}
			else {
				System.out.println("===> 공지사항이 없습니다..");
			}
			System.out.println();
		}
	}
	private static Document getPageDocument(String url) {
		Document sampleDoc = null;
		try {
			sampleDoc = Jsoup.connect(url)
					.userAgent(userAgent)
					.header("Referer", "http://eclass2.hufs.ac.kr:8181/ilos/main/main_form.acl")
					.header("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
					.header("Content-Type", "text/html; charset=utf-8")
					.header("Accept-Encoding", "gzip, deflate")
					.header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
					.cookies(loginCookie) // 위에서 얻은 '로그인 된' 쿠키
					.method(Connection.Method.GET)
					.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sampleDoc;
	}
	
	/*
	 * E-CLASS connect GET
	 * http://eclass2.hufs.ac.kr:8181/ilos/st/course/eclass_room2.acl  ==> POST(학수번호코드를 넘긴다)
	 * http://eclass2.hufs.ac.kr:8181/ilos/st/course/submain_form.acl  ==> GET
	 * 모든 강의실 페이지는 "/ilos/st/course/submain_form.acl" 로 동일
	 * 그전에 인증같은 과정이 이루어져야 하는데 이때 "/ilos/st/course/eclass_room2.acl" 링크를 통해 학수코드를 포함한 데이터를 함께 POST
	 * 성공적으로 인증이 되면 JSON 응답을 통해 확인할 수 있음
	 * 그 후 다시 "/ilos/st/course/submain_form.acl" 를 GET 하면 해당 강의실 페이지를 불러 올 수 있다.
	 */
	private static void eclassRoomConnect(String lecCode) {
		// 전송할 폼 데이터
		Map<String, String> data = new HashMap<String, String>();
		data.put("KJKEY", lecCode);
		data.put("returnURI", "/ilos/st/course/submain_form.acl");
		data.put("encoding", "utf-8");

    	// POST (JSON 응답)
		// KJKEY=A20191U5510620101&returnURI=%252Filos%252Fst%252Fcourse%252Fsubmain_form.acl&encoding=utf-8
    	try {
			String jsoupStr = Jsoup.connect("http://eclass2.hufs.ac.kr:8181/ilos/st/course/eclass_room2.acl")
					.userAgent(userAgent)
					.timeout(3000)
					.ignoreContentType(true)
					.cookies(loginCookie) // 위에서 얻은 '로그인 된' 쿠키
					.data(data)
					.method(Connection.Method.POST)
					.execute().body();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	//System.out.println(jsoupStr);
	}
}
