package test1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WISParseTest {
	
	static Map<String, String> loginCookie = null;
	// Windows, Whale의 User Agent.
	static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Whale/1.4.64.6 Safari/537.36";
	
	public static void main(String[] args) throws IOException, Exception {
		// 자바스크립트 function 호출을 위해 Nashorn JavaScript 엔진 사용
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(new FileReader("./src/sha512.js"));
		
		// cast the script engine to an invocable instance
		Invocable invocable = (Invocable) engine;
		//Object result = invocable.invokeFunction("SHA512", "sdfdfsf");
		
		// parameter 값으로 form data 전송
		String sID = "201402783";
		String rawPW = readEntry("password: ");
		// 패스워드 해쉬값 리턴
		String sha512PW = invocable.invokeFunction("SHA512", rawPW).toString();
//		System.out.println(sha512PW);
		
		String loginURL = "https://webs.hufs.ac.kr/src08/jsp/login/LOGIN1011M.jsp?usr_id=" + sID +
							"&usr_pwd=" + sha512PW + "&gubun=o";

		// 로그인(POST) - HTTPS
    	Connection.Response response1 = Jsoup.connect(loginURL)
				.userAgent(userAgent)
				.timeout(3000)
				.header("Origin", "https://webs.hufs.ac.kr")
				.header("Referer", "https://webs.hufs.ac.kr/src08/jsp/index.jsp")
				.header("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
				.header("Content-Type", "application/x-www-form-urlencoded")
				.header("Connection", "keep-alive")
				.header("Accept-Encoding", "gzip, deflate, br")
				.header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
				.method(Connection.Method.POST)
				.execute();
		//System.out.println(response1.statusMessage());
		// 로그인 성공 후 얻은 쿠키.
		// 쿠키 중 TSESSION이라는 값을 확인할 수 있다.
		loginCookie = response1.cookies();
		//System.out.println(loginCookie.entrySet());
		
		if(loginCookie.containsKey("JSESSIONID")) {
			System.out.println("JSESSIONID FOUND!");
		}
		else {
			System.out.println("JSESSIONID NOT FOUND!!");
		}
		//SSLHtmlParser();
		// 종정시 취득성적 페이지 GET (SSL 연결)
		Document mainPage = getPageDocument2("https://webs.hufs.ac.kr/src08/jsp/grade/GRADE1030L_List.jsp?tab_lang=K");
		System.out.println(mainPage.toString());
//		
//		// 수강과목만 파싱
//		Elements e1 = mainPage.select("em[class=sub_open]");
//		List<String> lectureList = new ArrayList<String>(); // 수강과목 이름 리스트
//		List<String> lectureCodeList = new ArrayList<String>(); // 수강과목 코드 과 URL 리스트
//		
//		for(Element lec : e1) {
//			String lecRawTitle = lec.attr("title");
//			String lecRawCode = lec.attr("onclick");
//			lectureList.add(lecRawTitle.substring(0, lecRawTitle.length() - 9));
//			lectureCodeList.add(lecRawCode.substring(lecRawCode.indexOf('\'') + 1, lecRawCode.lastIndexOf('\'')));
//		}
//		
//		for(int i = 0; i < lectureList.size(); i++) {
//			
//			// Refresh connection on every lecture
//			eclassRoomConnect(lectureCodeList.get(i));
//			
//	    	// 강의실 공지사항 게시판 페이지  GET
//			Document eclassNoticePage = getPageDocument("http://eclass2.hufs.ac.kr:8181/ilos/st/course/notice_list_form.acl");
//			
//			// 공지사항 조지기
//			System.out.println("*** [" + lectureList.get(i) + "] 공지사항 최근 게시물 ***");
//			if(!eclassNoticePage.select("table[class=bbslist] tbody tr td[class=left]").isEmpty()) {
//				Elements noticeTitles = eclassNoticePage.select("table[class=bbslist] tbody tr");
//				// 가장 최근 공지사항 게시물의 제목과 게시 일자 출력
//				System.out.println("===> " + noticeTitles.get(0).select("td").get(1).text() + "\t" + 
//									noticeTitles.get(0).select("td").get(4).text());	
//			}
//			else {
//				System.out.println("===> 공지사항이 없습니다..");
//			}
//			System.out.println();
//		}
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
	
	private static Document getPageDocument2(String url) {
		Document sampleDoc = null;
		try {
			sampleDoc = Jsoup.connect(url)
					.userAgent(userAgent)
					.header("Upgrade-Insecure-Requests", "1")
					.header("Host", "webs.hufs.ac.kr")
					.header("Connection", "keep-alive")
					.header("Cache-Control", "max-age=0")
					.header("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
					.header("Content-Type", "text/html; charset=utf-8")
					.header("Accept-Encoding", "gzip, deflate, br")
					.header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
					.cookies(loginCookie) // 위에서 얻은 '로그인 된' 쿠키
					.method(Connection.Method.GET)
					.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sampleDoc;
	}
	
	private static void SSLHtmlParser() {
		
		try {
			URL url = new URL("https://webs.hufs.ac.kr/src08/jsp/grade/GRADE1030L_List.jsp?tab_lang=K");
	        
	        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
	 
	        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Whale/1.5.71.15 Safari/537.36");
	        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
	        conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
	        conn.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
	        String tCookie = "JSESSIONID=" + loginCookie.get("JSESSIONID");
	        System.out.println(tCookie);
	        conn.setRequestProperty("Cookie", tCookie);
	        
	     // SSL setting
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new TrustManager[] { new X509TrustManager() {

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// client certification check
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

					// Server certification check

					try {

						// Get trust store
						KeyStore trustStore = KeyStore.getInstance("JKS");
						String cacertPath = System.getProperty("java.home") + "/lib/security/cacerts"; // Trust store path
																										// should be
																										// different by
																										// system platform.
						trustStore.load(new FileInputStream(cacertPath), "changeit".toCharArray()); // Use default
																									// certification
																									// validation

						// Get Trust Manager
						TrustManagerFactory tmf = TrustManagerFactory
								.getInstance(TrustManagerFactory.getDefaultAlgorithm());
						tmf.init(trustStore);
						TrustManager[] tms = tmf.getTrustManagers();
						((X509TrustManager) tms[0]).checkServerTrusted(chain, authType);

					} catch (KeyStoreException e) {
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			} }, null);
	        conn.setSSLSocketFactory(context.getSocketFactory());
	        
	        conn.connect();
	        conn.setInstanceFollowRedirects(true);
	 
	        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
	        BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            System.out.printf("%s\n", line);
	        }
	 
	        bis.close();
	        reader.close();
	        conn.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
