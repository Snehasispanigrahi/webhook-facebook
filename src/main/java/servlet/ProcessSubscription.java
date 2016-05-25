package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Enumeration;
import java.util.jar.Attributes.Name;

import javax.print.attribute.standard.MediaSize.NA;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet(name = "ProcessSubscription", urlPatterns = { "/Subscription" })
public class ProcessSubscription extends HttpServlet {
	
	/**
	 * @param args
	 */
	
	static String app_Secret = "b6e08769dc3d3421f9677855ba852013";
	static String app_Id = "521073994746729";
	static String app_AccessToken = "521073994746729|ZupZl-GaTXZO9cwrogu88U16NFA";
	
	/*static{
//		https://graph.facebook.com//oauth/access_token?client_id=<app-id>&client_secret=<app-secret>&grant_type=client_credentials
//		https://graph.facebook.com//oauth/access_token?client_id=521073994746729&client_secret=b6e08769dc3d3421f9677855ba852013&grant_type=client_credentials
		String url = "https://graph.facebook.com//oauth/access_token?client_id="+ app_Id +"&client_secret=" + app_Secret + "&grant_type=client_credentials";
		try {
			URL urldemo = new URL(url);
			URLConnection uc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				System.out.println("app accessToken : "+inputLine);
			app_AccessToken = inputLine;
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}*/
	
	@Override
	public void init() throws ServletException {
		String url = "https://graph.facebook.com//oauth/access_token?client_id="+ app_Id +"&client_secret=" + app_Secret + "&grant_type=client_credentials";
		try {
			URL urldemo = new URL(url);
			URLConnection uc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			StringBuilder stringBuilder = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null){
				System.out.println("app accessToken : "+inputLine);
//				app accessToken : access_token=521073994746729|ZupZl-GaTXZO9cwrogu88U16NFA
				stringBuilder.append(inputLine);
			}
			app_AccessToken = stringBuilder.toString();
			System.out.println("App accessToken stored in static var: "+app_AccessToken);
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Inside service");

		super.service(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Inside doGet");
		super.doGet(req, resp);
	}
	
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Inside doPost");
		/* GET /oauth/access_token?  
	    grant_type=fb_exchange_token&amp;           
	    client_id={app-id}&amp;
	    client_secret={app-secret}&amp;
	    fb_exchange_token={short-lived-token}  */
	    /* app-id:521073994746729
	    secret:b6e08769dc3d3421f9677855ba852013 */
		
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paraName = (String) parameterNames.nextElement();
			System.out.println("Para name: "+paraName+", Value: "+request.getParameter(paraName));
//			Para name: page_id, Value: 1376204942612961
//			Para name: access_token, 
//			Value: EAAHZA6f5by2kBAMvFskTVhsDr3GoW6ljIMrhN5xMiYBvraz1kXBzVf7CX4TTFyL5UW5CMYxRhJenplDEb9bwCkCmRsSIQccmsgk8CHy1ZCWPToLqEvSYYeM9B0eahCy0YQ8HDpNkKh92JRAIWxBonvpWDB2u0mxPGgNxl6QjyBgfIfgV2l
		}
		
		String pageId = request.getParameter("page_id");
        String page_accessToken = request.getParameter("access_token");
		
        System.out.println("app access token: "+app_AccessToken);
        
        if(app_AccessToken != null && page_accessToken != null){
//        	https://graph.facebook.com/debug_token?input_token=<page-short-live-access-token>&access_token=<app-accesstoken>
        	try {
				String url = "https://graph.facebook.com/debug_token?input_token="+ page_accessToken +"&" + app_AccessToken;
				System.out.println("url: "+url);
				//url: https://graph.facebook.com/debug_token?input_token=EAAHZA6f5by2kBAMvFskTVhsDr3GoW6ljIMrhN5xMiYBvraz1kXBzVf7CX4TTFyL5UW5CMYxRhJenplDEb9bwCkCmRsSIQccmsgk8CHy1ZCWPToLqEvSYYeM9B0eahCy0YQ8HDpNkKh92JRAIWxBonvpWDB2u0mxPGgNxl6QjyBgfIfgV2l&access_token=521073994746729|ZupZl-GaTXZO9cwrogu88U16NFA
				URL urldemo = new URL(url);
				URLConnection uc = urldemo.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				String inputLine;
				StringBuilder debugATResponse = new StringBuilder();
				while ((inputLine = in.readLine()) != null){
					System.out.println("debug the page access token"+inputLine);
					debugATResponse.append(inputLine);
				}
				/*debug the page access token
				{
				    "data": {
				        "app_id": "521073994746729",
				        "application": "Lead Generating App",
				        "expires_at": 1464174000,
				        "is_valid": true,
				        "profile_id": "1376204942612961",
				        "scopes": [
				            "manage_pages",
				            "public_profile"
				        ],
				        "user_id": "1137096926311693"
				    }
				}*/
				
				JsonParser parser = new JsonParser();
				JsonElement jElt = parser.parse(debugATResponse.toString());
				
				
				if(jElt.isJsonObject()){
					JsonObject jsonObj = (JsonObject) jElt;
					JsonObject data = (JsonObject) jsonObj.get("data");
					JsonElement app_id = data.get("app_id");
					System.out.println("app_id: "+app_id.getAsString());
					
					JsonElement expires_at = data.get("expires_at");
					System.out.println("expires_at: "+new Date(expires_at.getAsLong()));
					
					JsonElement is_valid = data.get("is_valid");
					System.out.println("is_valid: "+is_valid.getAsBoolean());
				}
				
				//check the expires at, before 5days we'll try regenerating 
				
				//generate url
				/*https://graph.facebook.com/oauth/access_token?
					grant_type=fb_exchange_token&
					client_id=521073994746729&
					client_secret=b6e08769dc3d3421f9677855ba852013&
					fb_exchange_token=EAAHZA6f5by2kBAKStpAzrpl6bOzDV9YQ49rfwNTFUpGZBC4GLuD0AyA6gd48N3EIe1hduxEG5eZAaROZApj019dZCwXouiZCsfRCdB6VAOZAkAhkklZBNYXwnoKrq8AgA1NoocPagYHfW3oyAOQUd9i98D5HqleLBYFuy1YSVo3dnypuHldKBeQq*/

				
				
				
				in.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }

/*        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();*/
      /*  
        if(page_acessToken != null){
//        	https://graph.facebook.com/oauth/client_code?access_token=...&amp;client_secret=...&amp;redirect_uri=...&amp;client_id=...
        	String url = "https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token&amp;client_id="+ app_Id +"&amp;client_secret=" + app_Secret + "&amp;fb_exchange_token=" + page_acessToken;
			URL urldemo = new URL(url);
			URLConnection yc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				System.out.println(inputLine);
			in.close();        
		}*/
	}

}
