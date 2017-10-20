package util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

/*import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;*/




public class HTTP 
{
	private static final String fqcn = HTTP.class.getName();
	private static final String userAgent = "Mozilla/5.0";
	private static final String contentTypeURLENCODED="application/x-www-form-urlencoded";
	private static final String contentTypeJSON="application/json";
	
	public static String doPost(String url,String requestBody) throws IOException
	{
		String mn = "doIO(POST : " + url + ", " + requestBody+" )";
		System.out.println(fqcn + " :: " + mn);
		URL restURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", userAgent);
		//conn.setRequestProperty("Content-Type", contentTypeURLENCODED);
		conn.setRequestProperty("Content-Type", contentTypeJSON);
		conn.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(requestBody);
		wr.flush();
		wr.close();
		int responseCode = conn.getResponseCode();
		System.out.println(fqcn + " :: " + mn + " : " + "Sending 'HTTP POST' request");
		System.out.println(fqcn + " :: " + mn + " : "+ "Response Code : " + responseCode);
		
		
		System.out.println("Response code is :" + conn.getResponseCode());
		if (conn.getResponseCode()>210) {
			BufferedReader in= new BufferedReader(
					new InputStreamReader(conn.getErrorStream()));

			String inputLine;
			StringBuilder jsonResponse = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				jsonResponse.append(inputLine);
			}
			in.close();
			System.out.println("Received error is " +jsonResponse);
			return new String(jsonResponse);
             
		}
		
		
		else{
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuilder jsonResponse = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			jsonResponse.append(inputLine);
		}
		in.close();
		System.out.println(fqcn + " :: " + mn + " : "+ jsonResponse.toString());
		return new String(jsonResponse);
		}
	}
	
	
	
	public static String doPostUser(String url, Map<String,String> sessionTokens, String requestBody,boolean isEncodingNeeded) throws IOException
	{
		String mn = "doIO(POST : " + url + ", " + requestBody+ "sessionTokens : " + sessionTokens  + " )";
		System.out.println(fqcn + " :: " + mn);
		URL restURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", userAgent);
		if(isEncodingNeeded)
			//conn.setRequestProperty("Content-Type", contentTypeURLENCODED);
			conn.setRequestProperty("Content-Type", contentTypeJSON);
		else
			conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");

		conn.setRequestProperty("Authorization", sessionTokens.toString() );
		conn.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(requestBody);
		wr.flush();
		wr.close();
		int responseCode = conn.getResponseCode();
		System.out.println(fqcn + " :: " + mn + " : " + "Sending 'HTTP POST' request");
		System.out.println(fqcn + " :: " + mn + " : "+ "Response Code : " + responseCode);
		
		if (conn.getResponseCode()>210) {
			BufferedReader in= new BufferedReader(
					new InputStreamReader(conn.getErrorStream()));

			String inputLine;
			StringBuilder jsonResponse = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				jsonResponse.append(inputLine);
			}
			in.close();
			System.out.println("Received error is " +jsonResponse);
			return new String(jsonResponse);
		}
		else{
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuilder jsonResponse = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			jsonResponse.append(inputLine);
		}
		in.close();
		System.out.println(fqcn + " :: " + mn + " : "+ jsonResponse.toString());
		return new String(jsonResponse);
		
		}
	}
	
	
	public static String doGet(String url, Map<String,String> sessionTokens) throws IOException, URISyntaxException
	{
		String mn = "doIO(GET :" + url+ ", sessionTokens =  " + sessionTokens.toString() +" )";
		System.out.println(fqcn + " :: " + mn);
		URL myURL = new URL(url);
		System.out.println(fqcn + " :: " + mn + ": Request URL=" + url.toString());
		HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
		//conn.setRequestMethod("GET");
		conn.setRequestProperty("User-Agent", userAgent);
		//conn.setRequestProperty("Content-Type", contentTypeJSON);
		//conn.setRequestProperty("Accept",);
		conn.setRequestProperty("Authorization", sessionTokens.toString() );
		conn.setDoOutput(true);
		System.out.println(fqcn + " :: " + mn + " : " + "Sending 'HTTP GET' request");
		int responseCode = conn.getResponseCode();
		System.out.println(fqcn + " :: " + mn + " : "+ "Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(conn.getInputStream()));
		String inputLine;	
		StringBuilder jsonResponse = new StringBuilder();
		while ((inputLine = in.readLine()) != null) 
		{
			System.out.println(inputLine);
			jsonResponse.append(inputLine);
		}
		in.close();
		System.out.println(fqcn + " :: " + mn + " : "+ jsonResponse.toString());
		return new String(jsonResponse);
	}
	
	public static String doPut(String url, String param, Map<String,String> sessionTokens) throws IOException, URISyntaxException
	{
		String mn = "doIO(PUT :" + url + ", sessionTokens =  " + sessionTokens.toString() +" )";
		System.out.println(fqcn + " :: " + mn);
		param=param.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D").replace(",", "%2C").replace("[", "%5B").replace("]", "%5D").replace(":", "%3A").replace(" ", "+");
		String processedURL = url+"?MFAChallenge="+param;//"%7B%22loginForm%22%3A%7B%22formType%22%3A%22token%22%2C%22mfaTimeout%22%3A%2299380%22%2C%22row%22%3A%5B%7B%22id%22%3A%22token_row%22%2C%22label%22%3A%22Security+Key%22%2C%22form%22%3A%220001%22%2C%22fieldRowChoice%22%3A%220001%22%2C%22field%22%3A%5B%7B%22id%22%3A%22token%22%2C%22name%22%3A%22tokenValue%22%2C%22type%22%3A%22text%22%2C%22value%22%3A%22123456%22%2C%22isOptional%22%3Afalse%2C%22valueEditable%22%3Atrue%2C%22maxLength%22%3A%2210%22%7D%5D%7D%5D%7D%7D";
		URL myURL = new URL(processedURL);
		System.out.println(fqcn + " :: " + mn + ": Request URL=" + processedURL.toString());
		HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
		conn.setRequestMethod("PUT");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setRequestProperty("Content-Type",contentTypeURLENCODED );
		conn.setRequestProperty("Authorization", sessionTokens.toString() );
		conn.setDoOutput(true);
		System.out.println(fqcn + " :: " + mn + " : " + "Sending 'HTTP PUT' request");
		int responseCode = conn.getResponseCode();
		System.out.println(fqcn + " :: " + mn + " : "+ "Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(conn.getInputStream()));
		String inputLine;	
		StringBuilder jsonResponse = new StringBuilder();
		while ((inputLine = in.readLine()) != null) 
		{
			System.out.println(inputLine);
			jsonResponse.append(inputLine);
		}
		in.close();
		System.out.println(fqcn + " :: " + mn + " : "+ jsonResponse.toString());
		return new String(jsonResponse);
	}
	

	public static String doPutNew(String url, String param, Map<String,String> sessionTokens) throws IOException, URISyntaxException
	{
		String mn = "doIO(PUT :" + url + ", sessionTokens =  " + sessionTokens.toString() +" )";
		System.out.println(fqcn + " :: " + mn);
		//param=param.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D").replace(",", "%2C").replace("[", "%5B").replace("]", "%5D").replace(":", "%3A").replace(" ", "+");
		String processedURL = url;//+"?MFAChallenge="+param;//"%7B%22loginForm%22%3A%7B%22formType%22%3A%22token%22%2C%22mfaTimeout%22%3A%2299380%22%2C%22row%22%3A%5B%7B%22id%22%3A%22token_row%22%2C%22label%22%3A%22Security+Key%22%2C%22form%22%3A%220001%22%2C%22fieldRowChoice%22%3A%220001%22%2C%22field%22%3A%5B%7B%22id%22%3A%22token%22%2C%22name%22%3A%22tokenValue%22%2C%22type%22%3A%22text%22%2C%22value%22%3A%22123456%22%2C%22isOptional%22%3Afalse%2C%22valueEditable%22%3Atrue%2C%22maxLength%22%3A%2210%22%7D%5D%7D%5D%7D%7D";
		URL myURL = new URL(processedURL);
		System.out.println(fqcn + " :: " + mn + ": Request URL=" + processedURL.toString());
		HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
		conn.setRequestMethod("PUT");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setRequestProperty("Content-Type",contentTypeJSON );
		conn.setRequestProperty("Authorization", sessionTokens.toString() );
		conn.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(param);
		wr.flush();
		wr.close();
		System.out.println(fqcn + " :: " + mn + " : " + "Sending 'HTTP PUT' request");
		int responseCode = conn.getResponseCode();
		System.out.println(fqcn + " :: " + mn + " : "+ "Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(conn.getInputStream()));
		String inputLine;	
		StringBuilder jsonResponse = new StringBuilder();
		while ((inputLine = in.readLine()) != null) 
		{
			System.out.println(inputLine);
			jsonResponse.append(inputLine);
		}
		in.close();
		System.out.println(fqcn + " :: " + mn + " : "+ jsonResponse.toString());
		return new String(jsonResponse);
	}
	
	
	public static String doDelete(String url, Map<String,String> sessionTokens) throws IOException, URISyntaxException
	{
		String mn = "doIO(Delete :" + url+ ", sessionTokens =  " + sessionTokens.toString() +" )";
		System.out.println(fqcn + " :: " + mn);
		URL myURL = new URL(url);
		System.out.println(fqcn + " :: " + mn + ": Request URL=" + url.toString());
		HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
		conn.setRequestMethod("DELETE");
		conn.setRequestProperty("User-Agent", userAgent);
		//conn.setRequestProperty("Content-Type", contentTypeJSON);
		//conn.setRequestProperty("Accept",);
		conn.setRequestProperty("Authorization", sessionTokens.toString() );
		conn.setDoOutput(true);
		System.out.println(fqcn + " :: " + mn + " : " + "Sending 'HTTP GET' request");
		int responseCode = conn.getResponseCode();
		System.out.println(fqcn + " :: " + mn + " : "+ "Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(conn.getInputStream()));
		String inputLine;	
		StringBuilder jsonResponse = new StringBuilder();
		while ((inputLine = in.readLine()) != null) 
		{
			System.out.println(inputLine);
			jsonResponse.append(inputLine);
		}
		in.close();
		System.out.println(fqcn + " :: " + mn + " : "+ jsonResponse.toString());
		return new String(jsonResponse);
	}
	
	
	
	
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}



	public static String doPostRegisterUser(String url, String registerParam,Map<String,String> cobTokens,boolean isEncodingNeeded) throws IOException{
		//String processedURL = url+"?registerParam="+registerParam;
		/* String registerUserJson="{"
				+ "		\"user\" : {"
				+ "				\"loginName\" : \"TestT749\","
				+ "				\"password\" : \"TESTT@123\","
				+ "				\"email\" : \"testet@yodlee.com\","
				+ "				\"firstName\" : \"Rehhsty\","
				+ "				\"lastName\" :\"ysl\""
				+"             } ,"
				
				+ "		\"preference\" : {"
				+ "		\"address\" : {"
				+ "				\"street\" : \"abcd\","
				+ "				\"state\" : \"CA\","
				+ "				\"city\" : \"RWS\","
				+ "				\"postalCode\" : \"98405\","
				+ "				\"countryIsoCode\" : \"USA\""
				+"             } ,"
				+ "				\"currency\" : \"USD\","
				+ "				\"timeZone\" : \"PST\","
				+ "				\"dateFormat\" : \"MM/dd/yyyy\""
				+ "}"
				+ "}";*/

		//encoding url
		registerParam = java.net.URLEncoder. encode(registerParam, "UTF-8");
		 String processedURL = url+"?registerParam="+registerParam;
		String mn = "doIO(POST : " + processedURL + ", " + registerParam+ "sessionTokens : "  +" )";
		System.out.println(fqcn + " :: " + mn);
		URL restURL = new URL(processedURL);
		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", userAgent);
		conn.setRequestProperty("Content-Type", contentTypeURLENCODED);
		conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
		conn.setRequestProperty("Authorization", cobTokens.toString() );
		conn.setDoOutput(true);
        conn.setRequestProperty("Accept-Charset", "UTF-8");
		int responseCode = conn.getResponseCode();
		if(responseCode == 200){
		System.out.println(fqcn + " :: " + mn + " : " + "Sending 'HTTP POST' request");
		System.out.println(fqcn + " :: " + mn + " : "+ "Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuilder jsonResponse = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			jsonResponse.append(inputLine);
		}
		in.close();
		System.out.println(fqcn + " :: " + mn + " : "+ jsonResponse.toString());
		return new String(jsonResponse);
		}else{
			System.out.println("Invalid input");
			return new String();

		}
	}

	
	
}
