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
	// Windows, Whale�� User Agent.
	static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Whale/1.4.64.6 Safari/537.36";
	
	public static void main(String[] args) throws IOException, Exception {
		// �ڹٽ�ũ��Ʈ function ȣ���� ���� Nashorn JavaScript ���� ���
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(new FileReader("./src/sha512.js"));
		
		// cast the script engine to an invocable instance
		Invocable invocable = (Invocable) engine;
		//Object result = invocable.invokeFunction("SHA512", "sdfdfsf");
		
		// parameter ������ form data ����
		String sID = "201402783";
		String rawPW = readEntry("password: ");
		// �н����� �ؽ��� ����
		String sha512PW = invocable.invokeFunction("SHA512", rawPW).toString();
//		System.out.println(sha512PW);
		
		String loginURL = "https://webs.hufs.ac.kr/src08/jsp/login/LOGIN1011M.jsp?usr_id=" + sID +
							"&usr_pwd=" + sha512PW + "&gubun=o";

		// �α���(POST) - HTTPS
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
		// �α��� ���� �� ���� ��Ű.
		// ��Ű �� TSESSION�̶�� ���� Ȯ���� �� �ִ�.
		loginCookie = response1.cookies();
		//System.out.println(loginCookie.entrySet());
		
		if(loginCookie.containsKey("JSESSIONID")) {
			System.out.println("JSESSIONID FOUND!");
		}
		else {
			System.out.println("JSESSIONID NOT FOUND!!");
		}
		//SSLHtmlParser();
		// ������ ��漺�� ������ GET (SSL ����)
		Document mainPage = getPageDocument2("https://webs.hufs.ac.kr/src08/jsp/grade/GRADE1030L_List.jsp?tab_lang=K");
		System.out.println(mainPage.toString());
//		
//		// �������� �Ľ�
//		Elements e1 = mainPage.select("em[class=sub_open]");
//		List<String> lectureList = new ArrayList<String>(); // �������� �̸� ����Ʈ
//		List<String> lectureCodeList = new ArrayList<String>(); // �������� �ڵ� �� URL ����Ʈ
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
//	    	// ���ǽ� �������� �Խ��� ������  GET
//			Document eclassNoticePage = getPageDocument("http://eclass2.hufs.ac.kr:8181/ilos/st/course/notice_list_form.acl");
//			
//			// �������� ������
//			System.out.println("*** [" + lectureList.get(i) + "] �������� �ֱ� �Խù� ***");
//			if(!eclassNoticePage.select("table[class=bbslist] tbody tr td[class=left]").isEmpty()) {
//				Elements noticeTitles = eclassNoticePage.select("table[class=bbslist] tbody tr");
//				// ���� �ֱ� �������� �Խù��� ����� �Խ� ���� ���
//				System.out.println("===> " + noticeTitles.get(0).select("td").get(1).text() + "\t" + 
//									noticeTitles.get(0).select("td").get(4).text());	
//			}
//			else {
//				System.out.println("===> ���������� �����ϴ�..");
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
					.cookies(loginCookie) // ������ ���� '�α��� ��' ��Ű
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
					.cookies(loginCookie) // ������ ���� '�α��� ��' ��Ű
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
	 * http://eclass2.hufs.ac.kr:8181/ilos/st/course/eclass_room2.acl  ==> POST(�м���ȣ�ڵ带 �ѱ��)
	 * http://eclass2.hufs.ac.kr:8181/ilos/st/course/submain_form.acl  ==> GET
	 * ��� ���ǽ� �������� "/ilos/st/course/submain_form.acl" �� ����
	 * ������ �������� ������ �̷������ �ϴµ� �̶� "/ilos/st/course/eclass_room2.acl" ��ũ�� ���� �м��ڵ带 ������ �����͸� �Բ� POST
	 * ���������� ������ �Ǹ� JSON ������ ���� Ȯ���� �� ����
	 * �� �� �ٽ� "/ilos/st/course/submain_form.acl" �� GET �ϸ� �ش� ���ǽ� �������� �ҷ� �� �� �ִ�.
	 */
	private static void eclassRoomConnect(String lecCode) {
		// ������ �� ������
		Map<String, String> data = new HashMap<String, String>();
		data.put("KJKEY", lecCode);
		data.put("returnURI", "/ilos/st/course/submain_form.acl");
		data.put("encoding", "utf-8");

    	// POST (JSON ����)
		// KJKEY=A20191U5510620101&returnURI=%252Filos%252Fst%252Fcourse%252Fsubmain_form.acl&encoding=utf-8
    	try {
			String jsoupStr = Jsoup.connect("http://eclass2.hufs.ac.kr:8181/ilos/st/course/eclass_room2.acl")
					.userAgent(userAgent)
					.timeout(3000)
					.ignoreContentType(true)
					.cookies(loginCookie) // ������ ���� '�α��� ��' ��Ű
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
