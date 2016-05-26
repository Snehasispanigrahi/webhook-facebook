package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@WebServlet(name = "ProcessSubscription", urlPatterns = { "/Subscription" })
public class ProcessSubscription extends HttpServlet {

	/**
	 * @param args
	 */

	static String app_Secret = "b6e08769dc3d3421f9677855ba852013";
	static String app_Id = "521073994746729";
	static String app_AccessToken = "521073994746729|ZupZl-GaTXZO9cwrogu88U16NFA";

	@Override
	public void init() throws ServletException {
		// Getting the app access token. Its not required every time to be
		// generated. But as the fb documentation suggests not to depend on any
		// access token for long, I am doing this.
		// App access token depends on static parameter, so assuming it won't change.
		System.out.println("Inside init()");
		String url = "https://graph.facebook.com//oauth/access_token?client_id=" + app_Id + "&client_secret="
				+ app_Secret + "&grant_type=client_credentials";
		app_AccessToken = requestUrl(url);
		System.out.println("App accessToken stored in static var: " + app_AccessToken);
		
		try {
			storeInDB();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * After the fb login when user selects a specific page, we come here
		 * with the pageId and page_accessToken(short-lived)
		 */
		System.out.println("Inside doPost");
		/*
		 * GET /oauth/access_token? grant_type=fb_exchange_token&amp;
		 * client_id={app-id}&amp; client_secret={app-secret}&amp;
		 * fb_exchange_token={short-lived-token}
		 */
		/*
		 * app-id:521073994746729 secret:b6e08769dc3d3421f9677855ba852013
		 */

		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paraName = (String) parameterNames.nextElement();
			System.out.println("Para name: " + paraName + ", Value: " + request.getParameter(paraName));
			// Para name: page_id, Value: 1376204942612961
			// Para name: access_token,
			// Value:
			// EAAHZA6f5by2kBAMvFskTVhsDr3GoW6ljIMrhN5xMiYBvraz1kXBzVf7CX4TTFyL5UW5CMYxRhJenplDEb9bwCkCmRsSIQccmsgk8CHy1ZCWPToLqEvSYYeM9B0eahCy0YQ8HDpNkKh92JRAIWxBonvpWDB2u0mxPGgNxl6QjyBgfIfgV2l
		}

		String pageId_Store_In_DB = request.getParameter("page_id");
		String page_accessToken = request.getParameter("access_token");

		String pageAT_Store_In_DB = debugToken(page_accessToken);
		
		System.out.println("We need to store page_acess_token(Long lived): "+pageAT_Store_In_DB + " & pageid: "+pageId_Store_In_DB);

		
		/*
		 * response.setContentType("text/html;charset=UTF-8"); PrintWriter out =
		 * response.getWriter();
		 */
		/*
		 * if(page_acessToken != null){ //
		 * https://graph.facebook.com/oauth/client_code?access_token=...&amp;
		 * client_secret=...&amp;redirect_uri=...&amp;client_id=... String url =
		 * "https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token&amp;client_id="+
		 * app_Id +"&amp;client_secret=" + app_Secret +
		 * "&amp;fb_exchange_token=" + page_acessToken; URL urldemo = new
		 * URL(url); URLConnection yc = urldemo.openConnection(); BufferedReader
		 * in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		 * String inputLine; while ((inputLine = in.readLine()) != null)
		 * System.out.println(inputLine); in.close(); }
		 */
	}

	private String debugToken(String page_accessToken) {
		if (app_AccessToken != null && page_accessToken != null) {
			// https://graph.facebook.com/debug_token?input_token=<page-short-live-access-token>&access_token=<app-accesstoken>
			String url = "https://graph.facebook.com/debug_token?input_token=" + page_accessToken + "&"
					+ app_AccessToken;
			String debugATResponse = requestUrl(url);
			/*
			 * debug the page access token { "data": { "app_id":
			 * "521073994746729", "application": "Lead Generating App",
			 * "expires_at": 1464174000, "is_valid": true, "profile_id":
			 * "1376204942612961", "scopes": [ "manage_pages", "public_profile"
			 * ], "user_id": "1137096926311693" } }
			 */

			if (requireToExpandTheToken(debugATResponse))
				return getLongLivedToken(page_accessToken);
		}
		return page_accessToken;
	}

	private boolean requireToExpandTheToken(String debugATResponse) {
		try {
			JsonParser parser = new JsonParser();
			JsonElement jElt = parser.parse(debugATResponse.toString());
			if (jElt.isJsonObject()) {
				JsonObject jsonObj = (JsonObject) jElt;
				JsonObject data = (JsonObject) jsonObj.get("data");
				JsonElement app_id = data.get("app_id");
				System.out.println("app_id: " + app_id.getAsString());

				JsonElement expires_at = data.get("expires_at");
				Date expiryDate = new Date(expires_at.getAsLong());
				System.out.println("expires_at: " + expiryDate);

				JsonElement is_valid = data.get("is_valid");
				System.out.println("is_valid: " + is_valid.getAsBoolean());

				// check the expires at, before 5days we'll try regenerating
				Date now = new Date();
				long diff = now.getTime() - expiryDate.getTime();
				long diffHours = diff / (60 * 60 * 1000);
				System.out.println("difference in hrs: " + diffHours);
				if (diffHours < 48) {
					System.out.println("less than 48hrs, lets expand this token");
					return true;
				}
			} else {
				System.out.println("Response : "+debugATResponse+ " not is expeceted format.");
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private String getLongLivedToken(String page_accessToken) {
		String url = "https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token&client_id=" + app_Id
				+ "&client_secret=" + app_Secret + "&fb_exchange_token=" + page_accessToken;

		/*
		 * https://graph.facebook.com/oauth/access_token?
		 * grant_type=fb_exchange_token& client_id=521073994746729&
		 * client_secret=b6e08769dc3d3421f9677855ba852013& fb_exchange_token=
		 * EAAHZA6f5by2kBAKStpAzrpl6bOzDV9YQ49rfwNTFUpGZBC4GLuD0AyA6gd48N3EIe1hduxEG5eZAaROZApj019dZCwXouiZCsfRCdB6VAOZAkAhkklZBNYXwnoKrq8AgA1NoocPagYHfW3oyAOQUd9i98D5HqleLBYFuy1YSVo3dnypuHldKBeQq
		 */

		return requestUrl(url);
	}

	private String requestUrl(String url) {
		StringBuilder response = new StringBuilder();
		try {
			System.out.println("url: " + url);

			URL urldemo = new URL(url);
			URLConnection uc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("response" + inputLine);
				response.append(inputLine);
			}
			return response.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response.toString();
	}
	
	private static Connection getConnection() throws URISyntaxException, SQLException {
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);

	}
	
    public void storeInDB() throws URISyntaxException, SQLException {
        
        Connection connection = getConnection();
        
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
        stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
        while (rs.next()) {
            System.out.println("Read from DB: " + rs.getTimestamp("tick"));
        }
    }

}
