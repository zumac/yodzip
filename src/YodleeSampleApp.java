/*
 * Copyright (c) 2017 Yodlee, Inc. All Rights Reserved.
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import parser.GSONParser;
import util.HTTP;
import beans.AccessToken;
import beans.AccountsArray;
import beans.CobrandContext;
import beans.UserContext;

/**
 * Servlet implementation class YodleeSampleApp
 */
@WebServlet("/YodleeSampleApp")
public class YodleeSampleApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	static ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
	public static final String  localURLVer1= resourceBundle.getString("yodlee.APIURL");
	static String coBrandUserName = resourceBundle.getString("yodlee.coBrandUserName");
	static String coBrandPassword = resourceBundle.getString("yodlee.coBrandPassword");

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public YodleeSampleApp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String cobSession = null;
		String userSession = null;
		
		
		String action = (String)request.getParameter("action");
		
		try {
			cobSession = (String)request.getSession().getAttribute("cobSession");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(action!=null) {
			
			// Initialize the Sample App by doing cobrand login.
			if(action.equals("init")) {
				
				if(cobSession == null)
					cobSession = getCobrandSession();

				if(cobSession != null && cobSession.length() > 0) {
					request.getSession().setAttribute("cobSession", cobSession);
					sendAjaxResponse(response, "{'cobSession':'"+cobSession+"'}");
				}else {
					sendAjaxResponse(response, "{'error':'true', 'message':'Cobrand Configuration Check Failed. Please check settings in config.properties'}");
				}
				
			}else {
				//for all below functinoal calls userSession is needed.
				userSession = (String)request.getSession().getAttribute("userSession");
				
				
				
				if(userSession != null && userSession.length() > 0) {
					//Get list of users accounts. Uses GET /accounts API
					if(action.equals("getAccounts")) {
						
						String accountsJson = getUserAccounts(cobSession, userSession);
						sendAjaxResponse(response, accountsJson);
					}
					
					// Get list of user transactions for specific account.Uses GET /transactions API
					if(action.equals("getTransactions")) {
						
						System.out.println("getTransactions");
						
						String accountId = (String)request.getParameter("accountId");
						String transactionsJson = getTranactions(cobSession, userSession, accountId);
						sendAjaxResponse(response, transactionsJson);
						
					}
					
					//Deactivates users specific account. Uses DELETE /accounts/<account id> API
					if(action.equals("deleteAccount")) {
						
						String accountId = (String)request.getParameter("accountId");
						String deleteAccountResponse = deleteAccount(cobSession, userSession, accountId);
						sendAjaxResponse(response, deleteAccountResponse);
						
					}
					
					//Obtain security token needed to launch FastLink.
					if(action.equals("getFastLinkToken")) {
						
						String fastLinkToken = getFastLinkToken(cobSession, userSession);
						
						String tokens = "{\"userSession\":\""+userSession+"\",\"fastlinkToken\":\""+fastLinkToken+"\"}";
						
						sendAjaxResponse(response, tokens);
						
					}
					
					if(action.equals("logout")) {
						//no functionality from servlet. Redirect to login page.
					}
				}else {
					sendAjaxResponse(response, "{'error':'true', 'message':'User session is not valid, please login again.'}");

				}
				

			}
			
			
		}else {
			sendAjaxResponse(response, "{'error':'true', 'message':'Missing Action parameter'}");
		}
		
	}

	private void sendAjaxResponse(HttpServletResponse response,
			String responseString) throws IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
		response.setCharacterEncoding("UTF-8"); // You want world domination, huh?
		response.getWriter().write(responseString);  
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String userSession = null;
		
		String userName = (String) request.getParameter("username");
		String password = (String) request.getParameter("password");
		
		String cobSession = (String) request.getSession().getAttribute("cobSession");
		
		try {
			userSession = userLogin(cobSession, userName, password);
			request.getSession().setAttribute("userSession", userSession);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(userSession != null) {
			
			//response.sendRedirect("accounts.html");
			sendAjaxResponse(response, "{'error':'false', 'message':'User authentication successfull'}");

			
		}else {
			response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
			response.setCharacterEncoding("UTF-8"); // You want world domination, huh?
			response.getWriter().write("{'error':'true', 'message':'Error in user Login, Invalid user credentials.'}"); 
		}
		
	}
	
	
	private String getCobrandSession() {

		String cobSession = "";

		try {
			final String requestBody = "{" + "\"cobrand\":      {"
					+ "\"cobrandLogin\": " + "\"" + coBrandUserName + "\""
					+ "," + "\"cobrandPassword\": " + "\"" + coBrandPassword
					+ "\"" + "," + "\"locale\": \"en_US\"" + "}" + "}";

			String coBrandLoginURL = localURLVer1 + "v1/cobrand/login";
			String cobrandjsonResponse = HTTP.doPost(coBrandLoginURL,
					requestBody);
			System.out.println("cobrand response is :" + cobrandjsonResponse);
			CobrandContext coBrand = (CobrandContext) GSONParser.handleJson(
					cobrandjsonResponse, beans.CobrandContext.class);

			System.out.println(" The Cobrand response is : "
					+ cobrandjsonResponse);

			if (!cobrandjsonResponse.contains("errorCode")) {
				cobSession = coBrand.getSession().getCobSession();
			}
			System.out.println("CobSession is :" + cobrandjsonResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return cobSession;

	}
	
	
	private String userLogin(String cobSession, String userName, String password) {
		// TODO Auto-generated method stub

		String userSession = null;

		try {
			Map<String, String> loginTokens = new HashMap<String, String>();
			loginTokens.put("cobSession", cobSession);

			// User login
			String userLoginURL = localURLVer1 + "v1/user/login";
			final String requestBody2 = "{" + "\"user\":      {"
					+ "\"loginName\": " + "\"" + userName + "\"" + ","
					+ "\"password\": " + "\"" + password + "\"" + ","
					+ "\"locale\": \"en_US\"" + "}" + "}";

			String userjsonResponse = HTTP.doPostUser(userLoginURL,
					loginTokens, requestBody2, true);
			UserContext member = (UserContext) GSONParser.handleJson(
					userjsonResponse, beans.UserContext.class);

			System.out.println(" The User response is : " + userjsonResponse);
			if (!userjsonResponse.contains("errorCode")) {
				userSession = member.getUser().getSession().getUserSession();
			}
			System.out.println("userSession is :" + userSession);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userSession;

	}
	
	
	private String getUserAccounts(String cobSession, String userSession) {
		// TODO Auto-generated method stub
		String accountURL = localURLVer1 + "v1/accounts";
		Map<String,String> loginTokens = new HashMap<String,String>();
		loginTokens.put("cobSession", cobSession);
		loginTokens.put("userSession",userSession);
		
		String accountJsonResponse=null;
		
		
		
		try {
			accountJsonResponse = HTTP.doGet(accountURL,loginTokens);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("accountJsonResponse:"+accountJsonResponse);

		
		return accountJsonResponse;
	}
	
	private String getTranactions(String cobSession, String userSession,
			String accountId) {
		
		String txnJson ="";
		
		String TransactionUrl = localURLVer1 + "v1/transactions" + "?fromDate=2013-01-01&accountId="+accountId;

		try {
			
			Map<String,String> loginTokens = new HashMap<String,String>();
			loginTokens.put("cobSession", cobSession);
			loginTokens.put("userSession",userSession);
			
			txnJson = HTTP.doGet(TransactionUrl,loginTokens);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return txnJson;
	}
	
	
	private String deleteAccount(String cobSession, String userSession,
			String accountId) {
		String deleteAccountResponse = null;
		
		String deleteAccountURL = localURLVer1 + "v1/accounts/";
		try {
			
			Map<String,String> loginTokens = new HashMap<String,String>();
			loginTokens.put("cobSession", cobSession);
			loginTokens.put("userSession",userSession);
			
			HTTP.doDelete(deleteAccountURL+accountId, loginTokens);
			
			deleteAccountResponse = "success";
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return deleteAccountResponse;
	}
	
	private String getFastLinkToken(String cobSession, String userSession) {

		String fastLinkToken = null;
		
		String accesstokenJsonResponse = null;
		
		 String accessTokenURL= localURLVer1 + "v1/user/accessTokens?appIds=10003600";
	       
			
			try {
				
				Map<String,String> loginTokens = new HashMap<String,String>();
				loginTokens.put("cobSession", cobSession);
				loginTokens.put("userSession",userSession);
				
				accesstokenJsonResponse = HTTP.doGet(accessTokenURL,loginTokens);

				AccessToken userAccess = (AccessToken) GSONParser.handleJson(accesstokenJsonResponse, beans.AccessToken.class);
				System.out.println("Token  response: " +accesstokenJsonResponse);
				fastLinkToken=userAccess.getUser().getAccessTokens()[0].getValue();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		System.out.println("Token value is :" + fastLinkToken);
		
		return fastLinkToken;

	}
	
	
	
	

}
