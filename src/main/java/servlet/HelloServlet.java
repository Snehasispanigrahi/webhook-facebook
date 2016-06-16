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

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@WebServlet(name = "HelloServlet", urlPatterns = { "/webhook" })
public class HelloServlet extends HttpServlet {
	
	/*static String app_Secret = "b6e08769dc3d3421f9677855ba852013";
	static String app_Id = "521073994746729";
	static String app_AccessToken = "521073994746729|ZupZl-GaTXZO9cwrogu88U16NFA";*/
	
	
	//test app
	static String app_Id = "522204084633720";
	static String app_Secret = "a14d0904a7c73b529f208193ef4fd9f3";
	static String app_AccessToken = "521073994746729|ZupZl-GaTXZO9cwrogu88U16NFA";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// String jsonRequest = "{\"entry\": [{\"changes\": [{\"field\":
		// \"leadgen\", \"value\": {\"ad_id\": 0, \"form_id\": 945245845529885,
		// \"leadgen_id\": 974394229281713, \"created_time\": 1463468994,
		// \"page_id\": 1376204942612961, \"adgroup_id\": 0}}], \"id\":
		// \"1376204942612961\", \"time\": 1463468994}], \"object\": \"page\"}";
		String jsonRequest = "{\"data\":{\"app_id\":\"521073994746729\",\"application\":\"Lead Generating App\",\"expires_at\":1464174000,\"is_valid\":true,\"profile_id\":\"1376204942612961\",\"scopes\":[\"manage_pages\",\"public_profile\"],\"user_id\":\"1137096926311693\"}}";
		System.out.println("request: " + jsonRequest);

		JsonParser parser = new JsonParser();
		JsonElement jElt = parser.parse(jsonRequest);

		if (jElt.isJsonObject()) {
			JsonObject jsonObj = (JsonObject) jElt;
			JsonObject data = (JsonObject) jsonObj.get("data");
			JsonElement app_id = data.get("app_id");
			System.out.println("app_id: " + app_id.getAsString());

			JsonElement expires_at = data.get("expires_at");
			System.out.println("expires_at: " + new Date(expires_at.getAsLong()));

			JsonElement is_valid = data.get("is_valid");
			System.out.println("is_valid: " + is_valid.getAsBoolean());
		}
	}

	// private static class fbRequest{
	// private fbEntry[] entry;
	// private String object;

	private static class fbEntry {
		private fbChanges[] changes;
		private String id;
		private String time;

		private static class fbChanges {
			private String field;
			private fbValues value;

			private static class fbValues {
				private String ad_id;
				private String form_id;
				private String leadgen_id;
				private String created_time;
				private String page_id;
				private String adgroup_id;

				public String getLeadgen_id() {
					return leadgen_id;
				}

				public void setLeadgen_id(String leadgen_id) {
					this.leadgen_id = leadgen_id;
				}

				public String getForm_id() {
					return form_id;
				}

				public void setForm_id(String form_id) {
					this.form_id = form_id;
				}
			}
		}
	}

	/*
	 * public fbEntry[] getEntry() { return entry; }
	 * 
	 * public void setEntry(fbEntry[] entry) { this.entry = entry; }
	 * 
	 * public String getObject() { return object; }
	 * 
	 * public void setObject(String object) { this.object = object; }
	 * 
	 * }
	 */

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String challenge = "hub.challenge";
		String token = "hub.verify_token";
		String challengeValue = req.getParameter(challenge);
		String tokenValue = req.getParameter(token);
		ServletOutputStream out = resp.getOutputStream();
		// PrintWriter out = resp.getWriter();
		if (tokenValue != null & tokenValue.equals("abc123")) {
			out.write(challengeValue.getBytes());
			System.out.println("challenge: " + challengeValue);
		}
		out.flush();
		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BufferedReader bufferedReader = request.getReader();
		char[] charBuffer = new char[128];
		StringBuilder stringBuilder = new StringBuilder();
		try {
			if (bufferedReader != null) {
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		System.out.println("request came:" + stringBuilder.toString());

		String jsonRequest = stringBuilder.toString();

		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElt = jsonParser.parse(jsonRequest);

		if (jsonElt.isJsonObject()) {
			JsonObject jsonObj = (JsonObject) jsonElt;
			JsonArray entry = (JsonArray) jsonObj.get("entry");
			jsonElt = jsonObj.get("object");

			System.out.println("object: " + jsonElt.toString());

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			fbEntry[] leadList = gson.fromJson(entry, fbEntry[].class);
			System.out.println("leadList :" + leadList);
			if (leadList == null || leadList.length == 0)
				System.out.println("No leads!");
			else {
				System.out.println("Leads=" + leadList.length);

				for (fbEntry lead : leadList) {
					String externalSourceData = gson.toJson(lead);
					System.out.println("lead request view: " + externalSourceData);

					fbEntry.fbChanges[] changes = lead.changes;

					for (int i = 0; i < changes.length; i++) {
						fbEntry.fbChanges.fbValues value = changes[i].value;
						String form_id = value.getForm_id();
						
						String leadgen_id = value.getLeadgen_id();
						/*						
						 *	before processing should we check the access token's life? If its expired only way to get is by logging in again.
						 *	Page access tokens last up to 60 days (5184000 seconds), but more importantly, they last as long as the user access token that was used to acquire them. So they will be invalidated as soon as the user that you got them from:
						 *
						 *	1.logs out of FB.
						 *	2.changes password.
						 *	3.deauthorizes your application.
						 *	Basically, when you lose the user's token, you will lose the page's token. Instead, you should retrieve page access tokens once per user access token. If you throw out a user access token, throw out the page token. You should not be trying to store page access tokens for any significant period of time. Instead you should get them as needed and forget them when a user's session dies.
						 *	To get a new page access token:
						 *
						 *	https://graph.facebook.com/PAGEID?fields=access_token&access_token=USER_ACCESS_TOKEN
						 */
						System.out.println("Pull this complete udpate: " + leadgen_id);
						System.out.println("page_id: " + value.page_id);
						
						String access_token = null;
						try {
							access_token = readFromDB(value.page_id);
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						debugToken(access_token);
						
						pullTheFormName(form_id, access_token);
						
//						String access_token = "EAAHZA6f5by2kBAFQRnvVn60nlQckPapZBIrZBkLTKqXoK1xcp2uYZCO2Ne2zuZB13wkGxfK6SKyEnfZAtsfDSkiY5GG5tyWG3SnXJrABWFyauDZCwVOwi9EXZCoubduvXWk3ukZBtjjVGf92nezfeSckOPDspiH2t4dZCLf0KgeLLnN1ZCk6MSnWZCCy";
						try {
							String url = "https://graph.facebook.com/v2.6/" + leadgen_id + "?access_token="
									+ access_token;
							URL urldemo = new URL(url);
							URLConnection yc = urldemo.openConnection();
							BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
							String inputLine;
							while ((inputLine = in.readLine()) != null)
								System.out.println(inputLine);
							in.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}
		} else {
			System.out.println("Error: JSON is not an object");
		}

	}

	private void pullTheFormName(String form_id, String page_id) {
		String url = "https://graph.facebook.com/v2.6/" + form_id + "?access_token=" + page_id;
		String formName = requestUrl(url);
		System.out.println("formName: " + formName);
	}

	private String debugToken(String page_accessToken) {
		if (app_AccessToken != null && page_accessToken != null) {
			// https://graph.facebook.com/debug_token?input_token=<page-short-live-access-token>&access_token=<app-accesstoken>
			String url = "https://graph.facebook.com/debug_token?input_token=" + page_accessToken + "&access_token="
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
	
	private static Connection getConnection() throws URISyntaxException, SQLException {
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);

	}
	
    public String readFromDB(String pageId_Store_In_DB) throws URISyntaxException, SQLException {
        
        Connection connection = getConnection();
        String page_AT = null;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT page_access_token FROM access_token where page_id='"+pageId_Store_In_DB+"'");
        while (rs.next()) {
            System.out.println("Read from DB: " + rs.getString("page_access_token"));
            page_AT = rs.getString("page_access_token");
        }
        
        
        
        return page_AT;
    }
	
}
