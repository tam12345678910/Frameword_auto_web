package utilities;

import static io.restassured.RestAssured.given;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import commons.BasePage;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
public class RestAssuredAPIUtils extends BasePage {
	private static Cookies cookies = null;
	
	protected static Log log;
	static Cookie cookie = null;
	WebDriver driver;
	
	protected RestAssuredAPIUtils() {
		log = LogFactory.getLog(getClass());
	}
	
	public static Cookies getRestAssureCookiesFromSelenium(WebDriver driver) {
		Set<Cookie> seleniumCookies = driver.manage().getCookies();
		List<io.restassured.http.Cookie> restAssuredCookies = new ArrayList<>();
		for(Cookie cookie : seleniumCookies) {
			restAssuredCookies.add(new io.restassured.http.Cookie.Builder(cookie.getName(), cookie.getValue()).build());
		}
		
		return new Cookies(restAssuredCookies);
	}
	
	public static List<Cookie> getSeleniumCookiesFromRestAssure(Cookies restAssureCookies) {
		List<org.openqa.selenium.Cookie> seleniumCookies = new ArrayList<>();
		
		for(io.restassured.http.Cookie cookie : restAssureCookies) {
			seleniumCookies.add(new org.openqa.selenium.Cookie.Builder(cookie.getName(), cookie.getValue()).build());
		}
		
		return seleniumCookies;
	}
	
	
	public static Cookies loginPage(String baseUrl, String user, String password) {
		Response response = getResponseLoginAPI(baseUrl, user, password);
		Assert.assertNotEquals( response.getBody().prettyPrint(),"0", "Failed to login By API.");
		return response.getDetailedCookies();
	}
	
	public static Response getResponseLoginAPI(String baseUrl, String user, String password) {
		
		return given().baseUri("https://opensource-demo.orangehrmlive.com")
				.basePath("/index.php/auth/login").queryParam("", "")
				.contentType(ContentType.URLENC)
				.header("trust-code", encode(user))
				.formParam("login", user)
				.formParam("password", password)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().response();
	}
	
	private static String trustCodeSaltString = "sqIjs9VMzQo0pOM9OufIb6ko6RfeEpxvf5AIdi5IAKE";
	
	public static String encode(String loginId) {
		if(StringUtils.isBlank(loginId)) {
			 log.info("Warning: LoginId is blank");
		}
		byte[] loginIdBytes = loginId.trim().toLowerCase().getBytes(StandardCharsets.UTF_8);
		MessageDigest digest = md();
		
		byte[] salt = Base64.getUrlDecoder().decode(trustCodeSaltString);
		digest.update(salt);
		int infoSize = 4 + loginIdBytes.length + 8;
		int messageSize = infoSize + salt.length;
		byte[] buffer = new byte[messageSize];
		java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(buffer);
		bb.putInt(loginIdBytes.length);
		bb.put(loginIdBytes);
		bb.putLong(System.currentTimeMillis());
		digest.update(buffer, 0 , infoSize);
		bb.put(digest.digest());
		return Base64.getUrlEncoder().encodeToString(buffer);
		
	}
	
	protected static MessageDigest md() {
		try {
			return MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("not possbile, jdk requires sha-256");
		}
	}
	
//	public static Object getAPI(String fromDate, String toDate, String playerID) {
//		
//		if (cookies == null) {
//			cookies = loginPage("", "", "");
//		}
//		
//		List<LinkedHashMap<String, Object>> responeBody = null;
//		responeBody = given().baseUri("https://demo.org.com")
//				.basePath("link api")
//				.queryParam("", "")
//				.queryParam("", "")
//				.cookies(cookies)
//				.contentType("application/x-www-form-urlencoded")
//				.contentType(ContentType.URLENC.withCharset("UTF-8"))
//				.formParam("", "")
//				.when()
//				.post()
//				.then()
//				.statusCode(200)
//				.contentType(ContentType.JSON)
//				.extract()
//				.response()
//				.getBody()
//				.jsonPath().getList("records");
//		
//		return new Object.Builder().txtType(responeBody.get(0).get("").toString()).buildDeposit();
//	}
	
}
